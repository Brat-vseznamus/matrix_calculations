import kotlin.math.*

const val eps = 1e-9
fun getNearest(d : Double) : Double {
    return when {
        (abs(ceil(d) - d) <= eps) -> ceil(d)
        (abs(floor(d) - d) <= eps) -> floor(d)
        else -> d
    }

}