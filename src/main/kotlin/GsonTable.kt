package cn.bugsnet.gsontable

data class GsonTable<T>(val classOfElement: Class<T>, private val list: MutableList<T> = ArrayList()): MutableList<T> by list
