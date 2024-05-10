@file:Suppress("LocalVariableName","unused")
package id.hmd.itunesmovies.utils

import android.content.SharedPreferences

@Suppress("LocalVariableName","unused")
class LocalPreferences (private var sharedPreference: SharedPreferences) {

    fun savePrefrencesByPrefix(prefkey: String, datas: List<String>){
        val temp = ArrayList<String>()
        for (i in datas.indices){
            temp.add("$i${datas[i]}")
        }
        sharedPreference.edit()
            .putStringSet(prefkey,temp.toSet())
            .apply()
    }

    fun savePrefrencesByPrefix(prefkey: String, data: String){
        sharedPreference.edit()
            .putString(prefkey,data)
            .apply()
    }

    fun savePrefrencesByPrefix(prefkey: String, data: Int){
        sharedPreference.edit()
            .putInt(prefkey,data)
            .apply()
    }

    fun getPrefrencesByPrefix(prefkey: String): List<String> {
        val res = mutableListOf<String>()
        val data = sharedPreference.getStringSet(prefkey, HashSet())
        data?.let {
            val sorted = it.sorted()
            for (i in sorted){
                res.add(i.substring(1, i.length))
            }
        }
        return res
    }

    fun getStringPrefrencesByPrefix(prefkey: String): String? {
        return sharedPreference.getString(prefkey,"")
    }

    fun getIntPrefrencesByPrefix(prefkey: String): Int {
        return sharedPreference.getInt(prefkey,0)
    }

    fun deletePrefrences(prefkey: String){
        sharedPreference.edit()
            .remove(prefkey)
            .apply()
    }

    fun wipeLocalData(){
        sharedPreference.edit().clear().apply()
    }
}