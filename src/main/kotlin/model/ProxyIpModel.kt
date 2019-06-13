package model

class ProxyIpModel(val ipAddress:String, val port:String) {
    override fun toString(): String {
        return "ProxyIp(ip == '$ipAddress',port == '$port')\n"
    }
}