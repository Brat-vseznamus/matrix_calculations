import kotlin.math.max

class Window {
    private val topLeft: Pair<Int, Int>
    private val bottomRight: Pair<Int, Int>
    private val matrix: Matrix

    constructor(topLeft: Pair<Int, Int>, bottomRight: Pair<Int, Int>, matrix: Matrix) {
        this.topLeft = topLeft
        this.bottomRight = bottomRight
        this.matrix = matrix
    }

    constructor(topLeft: Pair<Int, Int>, bottomRight: Pair<Int, Int>, window: Window) {
        this.topLeft = topLeft + window.topLeft
        this.bottomRight = bottomRight + window.bottomRight
        this.matrix = window.matrix
    }

    constructor(matrix: Matrix) {
        this.topLeft = Pair(0, 0)
        this.bottomRight = Pair(matrix.getN() - 1, matrix.getM() - 1)
        this.matrix = matrix
    }

    operator fun get(i: Int, j : Int) : Double {
        return if (topLeft.first +  i > bottomRight.first
            || topLeft.second + j > bottomRight.second) {
            0.0
        } else {
            matrix[i + topLeft.first][j + topLeft.second]
        }
    }

    operator fun plus(window: Window) : Matrix {
        val sizeA = getSizes()
        val sizeB = window.getSizes()
        val sizes = Pair(
            max(sizeA.first, sizeB.first),
            max(sizeA.second, sizeB.second))
        val m = Matrix(sizes.first, sizes.second)
        for (i in 0 until sizes.first) {
            for (j in 0 until  sizes.second) {
                m[i][j] = this[i, j] + window[i, j]
            }
        }
        return m
    }

    fun getSizes() : Pair<Int, Int> {
        return (bottomRight - topLeft) + Pair(1, 1)
    }

}

private operator fun Pair<Int, Int>.plus(b: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first + b.first, this.second + b.second)
}

private operator fun Pair<Int, Int>.minus(b: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first - b.first, this.second - b.second)
}
