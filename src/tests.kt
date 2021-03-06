import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun main() {
//    var m = Matrix(arrayOf(doubleArrayOf(1.0, 1.0, 1.0)))
//    println(m.toString())
//    m = Matrix(
//        arrayOf(doubleArrayOf(1.0),
//        doubleArrayOf(1.0, 2.0, 0.0),
//        doubleArrayOf(1.0, 7.0)))
//    println(m * 3.0)
//    var m2 = Matrix(
//            arrayOf(doubleArrayOf(4.0),
//            doubleArrayOf(1.0, 2.0, 0.0),
//            doubleArrayOf(1.0, 5.0)))
//    println("original")
//    println(m * m2)
//    println("fast: ")
//    println(m.fastMultiply(m2))
//
//    println(m + m2)
//    val m3 = Matrix(1.0, 0.0, 1.0,
//                    2.0, 1.0, 1.0,
//                    3.0, 1.0, 1.0)
//    val e = Matrix.E(3)
//    println(m3 * e)
//    println(m3.determinant())
//    println(e.determinant())
//    println(e.invertible())
//
//    val m4 = Matrix(-1.0, 1.5,
//                        1.5, 1.0)
//    println(m4.invertible()!!.invertible())
//
//    m = Matrix( 9.0, 3.0, 7.0,
//                4.0, 1.0, 6.0,
//                1.0, 2.0, 5.0)
////    println(m * 3.0)
//    m2 = Matrix( 1.0, -6.0, 5.0,
//                 6.0, 5.0, 3.0,
//                 1.0, -2.0, 4.0)
//    println("original")
//    println(m * m2)
//    println("fast: ")
//    println((m * m2 / Matrix.E(3)).invertible())
    val v1 = Vector(1.0, 2.0, 3.0)
    println(v1)
    val theta = PI / 4
    val m = Matrix(cos(theta), -sin(theta), 0.0,
                    sin(theta),  cos(theta), 0.0,
                    0.0, 0.0,  1.0)
    println(m)
    println(m.invertible())
    println(m * v1)
    println(m.invertible() *(m * v1))

    println(m * m.invertible())

}