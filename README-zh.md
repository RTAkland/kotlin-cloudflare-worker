# Kotlin cloudflare worker

纯Kotlin编写的库，可以在Cloudflare worker上运行Kotlin/JS

# 快速开始

## 配置

> 项目依赖于Kotlin协程库, 请确保协程库版本号大于`1.10.2`

```kotlin
plugins {
    kotlin("multiplatform") version "2.2.21"
}

repositories {
    mavenCentral()
    maven("https://repo.maven.rtast.cn/releases")
}
```

```kotlin
// 最新版本请前往下方链接查看
// https://next.pkg.rtast.cn/#/releases/cn/rtast/kotlin-cfworker/kotlin-cloudflare-worker/
implementation("cn.rtast.kotlin-cfworker:kotlin-cloudflare-worker:1.0.2")
```

## Hello world程序

```kotlin
kotlin {
    js(IR) {
        nodejs {
            outputModuleName = "kotlin-cloudflare-worker"
            binaries.executable()
        }
    }
}
```

> Minimal hello world app

```kotlin
/**
 * 不要修改这个函数的任何内容
 */
fun main() {
    @Suppress("unused_expression")
    val eventListener = EventListener { event ->
        val dyn = event.asDynamic()
        event.asDynamic().respondWith(handleRequest(dyn.request as Request))
        Unit
    }
    js("addEventListener('fetch', eventListener)")
}

/**
 * 这里是处理http请求的函数
 */
@JsExport
fun handleRequest(request: Request): Promise<Response> = GlobalScope.promise {
    val server = WorkerApplication().apply {
        route("/") {
            respondText("Hello kotlin cloudflare worker")
        }
    }
    return@promise server.handle(request)
}

```

> 在本地运行之前需要先安装wrangler

在项目根目录创建一个名为`wrangler.toml`的文件，然后将下面的内容复制进去并修改为你自己的信息

```toml
name = "kotlin-cloudflare-worker"  # 可以是任何名字
account_id = "<your account id>"
workers_dev = false
preview_urls = false
compatibility_date = "2022-08-11"
main = "kotlin-cloudflare-worker.js"  # 必须和`outputModuleName`一致
```

运行gradle任务编译出js `gradlew compileProductionExecutableKotlinJs`,
将wrangler.toml复制到 `build/compileSync/js/main/productionExecutable/kotlin`,
并且进入该目录下运行`wrangler dev`

## 部署

使用`wrangler deploy`来部署到cloudflare worker, 在此之前你需要先使用`wrangler login`来登录

# 注意事项

Cloudflare worker 使用V8引擎来驱动js脚本，线程模型为单线程，所有事件均由event loop(事件循环)驱动，在此之外的所有任务
都会被drop, 所以不要使用协程库中的launch或任何可以凭空创建出一个挂起作用域的函数来调用suspend函数, 取而代之的是你需要从
`handleRequest`方法中传播`CoroutineScope`(suspend), 就像下面这样

```kotlin
// 不要使用这种方法
val scope = CoroutineScope(Dispatcher.DEFAULT)

fun blockingFunction() {
    scope.launch {
        // 这里的代码永远不会被执行
    }
}

// 使用下面的代码来正确的调用suspend函数

fun main() {
    @Suppress("unused_expression")
    val eventListener = EventListener { event ->
        val dyn = event.asDynamic()
        event.asDynamic().respondWith(handleRequest(dyn.request as Request))
        Unit
    }
    js("addEventListener('fetch', eventListener)")
}

@JsExport
fun handleRequest(request: Request): Promise<Response> = GlobalScope.promise {
    return Promise.resolve(Response("Hello"))
}
```

# Http 客户端

不要使用ktor来作为http客户端, 因为cloudflare worker不是一个标准的nodejs环境， 你需要使用 fetch
`cn.rtast.cfworker.client.fetch` 来发送http请求, 注意不是`window.fetch`， `window.fetch`是浏览器API

# ByteArray 和 ByteBuffer 转换

```kotlin
import cn.rtast.cfworker.util.toByteArray
import cn.rtast.cfworker.util.toArrayBuffer

val bb: ByteBuffer = ...
// 将ByteBuffer转换为ByteArray
val ba: ByteArray = bb.toByteArray()

// 将ByteArray转换为ByteBuffer
val bb2: ByteBuffer = ba.toArrayBuffer()
```

# CPU时间限制

cloudflare worker 免费版本限制处理每个请求的CPU时间为10ms(不包括IO等待时间), 如果你需要执行CPU密集型任务或需要更高的配额，
请考虑升级到Workers paid或使用JS来减少标准库和协程库的加载时间.

即使使用此库注册了超过15个api端点，并且都具备文件处理功能，每个请求占用的cpu时间平均值不超过10ms+-2ms,
在冷启动时处理首次请求时间会大幅增加，最高可达50ms，但worker仍然可以处理并返回请求.

![avg-cpu-time](./images/avg-cpu-time.png)

> 上图反应了过去24小时每个请求的平均cpu时间(P50)

# 已经使用此项目的实例

https://repo.maven.rtast.cn