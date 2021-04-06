class Vector(n : Int) : Matrix(n, 1) {
    constructor(vararg xs : Double) : this(xs.size) {
        for(i in xs.indices) {
            data[i][0] = xs[i]
        }
    }

    override fun toString(): String {
        var str = "["
        for (x in data) {
            str += "${x[0]}, "
        }
        str = str.dropLast(2)
        return str + "]^T"
    }
}