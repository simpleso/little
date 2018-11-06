package top.andnux.little.annotations

@Target(allowedTargets = [AnnotationTarget.CLASS])
@Retention(AnnotationRetention.RUNTIME) //这一行也可以省略
annotation class Inject(val id: Int)
