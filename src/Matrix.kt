import kotlin.math.max

class Matrix {
    private var data : Array<DoubleArray>
    private val n : Int
    private val m : Int
    constructor() {
        n = 0;
        m = 0;
        data = arrayOf()
    }

    constructor(n : Int, m : Int) {
        this.n = n;
        this.m = m;
        this.data = Array(n) {DoubleArray(m)}
    }

    constructor(x00 : Double) {
        this.n = 1;
        this.m = 1;
        this.data = arrayOf(doubleArrayOf(x00))
    }

    constructor(x00 : Double, x01 : Double,
                x10 : Double, x11 : Double) {
        this.n = 2;
        this.m = 2;
        this.data = arrayOf(doubleArrayOf(x00, x01),
                            doubleArrayOf(x10, x11))
    }

    constructor(x00 : Double, x01 : Double, x02 : Double,
                x10 : Double, x11 : Double, x12 : Double,
                x20 : Double, x21 : Double, x22 : Double) {
        this.n = 3;
        this.m = 3;
        this.data = arrayOf(doubleArrayOf(x00, x01, x02),
                            doubleArrayOf(x10, x11, x12),
                            doubleArrayOf(x20, x21, x22))
    }

    constructor(data : Array<DoubleArray>) {
        this.n = data.size;
        this.m = data.maxOf { it -> it.size };
        this.data = Array(n) {DoubleArray(m)}
        for (arr in data.indices) {
            for (ind in data[arr].indices) {
                this.data[arr][ind] = data[arr][ind]
            }
        }
    }

    constructor(n : Int, m : Int, data : Array<DoubleArray>) {
        this.n = n;
        this.m = m;
        this.data = Array(n) {DoubleArray(m)}
        for (arr in data.indices) {
            for (ind in data[arr].indices) {
                this.data[arr][ind] = data[arr][ind]
            }
        }
    }

    companion object {
        fun E(n: Int) : Matrix {
            val e = Matrix(n, n)
            for (ind in 0 until n) {
                e[ind][ind] = 1.0
            }
            return e
        }
    }

    private fun elementCalculation(m : Matrix?, f : (Double, Double) -> Double) : Matrix? {
        if (m == null || !checkSameSize(m)) {
            return null
        }
        val res = Matrix(this.n, this.m)
        for (row in 0 until this.n) {
            for (col in 0 until this.m) {
                res.data[row][col] =
                    f.invoke(data[row][col], m[row][col])
            }
        }
        return res
    }
    private fun add(m : Matrix?) : Matrix? {
        return elementCalculation(m) { x, y -> x + y }
    }

    operator fun plus(m : Matrix?) : Matrix? {
        return add(m)
    }

    fun subtract(m : Matrix?) : Matrix? {
        return elementCalculation(m) { x, y -> x - y }
    }

    operator fun minus(m: Matrix?) : Matrix? {
        return subtract(m)
    }

    private fun multiplyByNumber(k : Double) : Matrix? {
        return elementCalculation(this) { x, _ -> x * k }
    }

    operator fun times(k : Double) : Matrix? {
        return multiplyByNumber(k)
    }

    operator fun times(m : Matrix?) : Matrix? {
        return fastMultiply(m)
    }

    // slow version O(n^3)
    private fun multiply(m : Matrix?) : Matrix? {
        if (m == null || this.m != m.n) {
            return null
        }
        val res = Matrix(this.n, m.m)
        for (row in 0 until this.n) {
            for (col in 0 until m.m) {
                res.data[row][col] = 0.0
                for (el in 0 until this.m) {
                    res[row][col] += this[row][el] * m[el][col]
                }
            }
        }
        return res
    }

    // O(n ^ (log2(7)))
    fun fastMultiply(m : Matrix?) : Matrix? {
        if (m == null || this.m != m.n) {
            return null
        }

        if (this.n == m.n
            && this.m == m.m
            && this.n == 1) {
            return Matrix(data[0][0] * m[0][0])
        }

        val maxSize = max(max(this.n, this.m),max(this.n, this.m))
        fun findExp(v : Int) : Int {
            var exp = 1
            while (exp < v) {
                exp *= 2
            }
            return exp
        }

        val exp = findExp(maxSize)
        val m1 = if (this.m == exp && this.n == exp) this else Matrix(exp, exp, this.data)
        val m2 = if (m.m == exp && m.n == exp) m else Matrix(exp, exp, m.data)

        fun createSubMatrices(m: Matrix) : Array<Array<Matrix>> {
            return Array(2) {
                    i : Int -> Array<Matrix>(2) {
                    j : Int -> m.subMatrix(
                    exp/2 * i,
                    exp/2 * j,
                    exp/2 * (i + 1) - 1,
                    exp/2 * (j + 1) - 1)
                }
            }
        }
        val a = createSubMatrices(m1)
        val b = createSubMatrices(m2)

        val p1 = (a[0][0] + a[1][1])?.fastMultiply((b[0][0] + b[1][1]))!!
        val p2 = (a[1][0] + a[1][1])?.fastMultiply(b[0][0])!!
        val p3 = a[0][0].fastMultiply(b[0][1] - b[1][1])!!
        val p4 = a[1][1].fastMultiply(b[1][0] - b[0][0])!!
        val p5 = (a[0][0] + a[0][1])?.fastMultiply(b[1][1])!!
        val p6 = (a[1][0] - a[0][0])?.fastMultiply(b[0][0] + b[0][1])!!
        val p7 = (a[0][1] - a[1][1])?.fastMultiply(b[1][0] + b[1][1])!!

        val c00 = p1 + (p4 + (p7 - p5))
        val c01 = p3 + p5
        val c10 = p2 + p4
        val c11 = p1 + (p3 + (p6 - p2))

        val res = Matrix(exp, exp)
        res.fitMatrix(c00!!, 0, 0, exp/2 - 1, exp/2 - 1)
        res.fitMatrix(c01!!, 0, exp/2, exp/2 - 1, exp - 1)
        res.fitMatrix(c10!!, exp/2, 0, exp - 1, exp/2 - 1)
        res.fitMatrix(c11!!, exp/2, exp/2, exp - 1, exp - 1)

        return res.subMatrix(0, 0, this.n - 1, m.m - 1)
    }

    private fun subMatrix(i0: Int, j0: Int, i1: Int, j1: Int) : Matrix {
        val res = Matrix(i1 - i0 + 1, j1 - j0 + 1)
        for (i in 0..(i1 - i0)) {
            for (j in 0..(j1 - j0)) {
                res[i][j] = data[i0 + i][j0 + j]
            }
        }
        return res
    }

    private fun fitMatrix(m: Matrix, i0: Int, j0: Int, i1: Int, j1: Int) {
        for (i in 0..(i1 - i0)) {
            for (j in 0..(j1 - j0)) {
                data[i0 + i][j0 + j] += m[i][j]
            }
        }
    }

    operator fun get(index : Int) : DoubleArray {
        return data[index]
    }

    override fun toString() : String {
        var res = ""
        for (row in 0 until this.n) {
            for (col in 0 until this.m) {
                res += this[row][col].toString()
                res += " "
            }
            res += '\n'
        }
        return res
    }

    private fun checkSameSize(m : Matrix?) : Boolean {
        return m != null && this.n == m.n && this.m == m.m
    }

    fun determinant() : Double? {
        if (n != m) {
            return null
        }
        if (n == 1) {
            return data[0][0]
        }
        var result = 0.0
        for (i in 0 until n) {
            var aij = data[0][i]
            if (i % 2 != 0) {
                aij *= -1.0
            }
            val subMatrix = minor(0, i)!!
            result += aij * subMatrix.determinant()!!
        }
        return result
    }

    operator fun div(m: Matrix?) : Matrix? {
        if (m == null) {
            return null
        }
        val det = m.determinant()
        if (det == 0.0
            || this.n != this.m
            || m.n != m.m
            || this.n != m.n) {
            return null
        }
        val res = Matrix(
            Array(n) {
                i ->
                DoubleArray(n) {
                    j -> m.minor(i, j)!!.determinant()!! *
                        if ((i + j) % 2 == 0)
                            1 else -1
                }
            }
        )
        return (this * res)!! * (1.0 / det!!)
    }

    fun invertible() : Matrix? {
        return E(n) / this
    }

    private fun minor(i: Int, j: Int) : Matrix? {
        if (n != m) {
            return null
        }
        val subMatrix = Matrix(n - 1, n - 1)
        var realrow = 0
        var realcol = 0
        for (row in 0 until (n - 1)) {
            realcol = 0
            if (row == i) {
                realrow++
            }
            for (col in 0 until (n - 1)) {
                if (col == j) {
                    realcol++
                }
                subMatrix[row][col] = this[realrow][realcol]
                realcol++
            }
            realrow++
        }
        return subMatrix
    }

}