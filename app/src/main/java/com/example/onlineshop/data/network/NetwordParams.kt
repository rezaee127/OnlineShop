package com.example.onlineshop.data.network

class NetworkParams {

    companion object{
        const val CONSUMER_KEY="ck_35f6bcc458eed45f8af8716c18772621ad139e13"
        const val CONSUMER_SECRET="cs_710d145f6e04fc53ad917475459e14bcda2c9630"
        const val BASE_URL = "https://woocommerce.maktabsharif.ir/wp-json/wc/v3/"
        const val PER_PAGE="100"

        fun getBaseOptions():Map<String,String>{
            val optionsHashMap=HashMap<String,String>()
            optionsHashMap["consumer_key"]=CONSUMER_KEY
            optionsHashMap["consumer_secret"]=CONSUMER_SECRET
            optionsHashMap["per_page"]= PER_PAGE
            return optionsHashMap
        }
    }
}