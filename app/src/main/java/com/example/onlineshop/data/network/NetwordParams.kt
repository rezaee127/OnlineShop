package com.example.onlineshop.data.network

class NetworkParams {

    companion object{

        private const val CONSUMER_KEY="ck_63f4c52da932ddad1570283b31f3c96c4bd9fd6f"
        private const val CONSUMER_SECRET="cs_294e7de35430398f323b43c21dd1b29f67b5370b"
        const val BASE_URL = "https://woocommerce.maktabsharif.ir/wp-json/wc/v3/"
        private const val PER_PAGE="100"

        fun getBaseOptions():Map<String,String>{
            val optionsHashMap = getBaseOptionsWithOutPerPage() as HashMap<String, String>
            optionsHashMap["per_page"]= PER_PAGE
            return optionsHashMap
        }

        fun getBaseOptionsWithOutPerPage():Map<String,String>{
            val optionsHashMap=HashMap<String,String>()
            optionsHashMap["consumer_key"]=CONSUMER_KEY
            optionsHashMap["consumer_secret"]=CONSUMER_SECRET
            return optionsHashMap
        }
    }
}