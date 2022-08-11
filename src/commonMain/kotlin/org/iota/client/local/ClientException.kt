package org.iota.client.local

class ClientException : RuntimeException {

    constructor() : super() {}
    constructor(errorMessage: String?) : super(errorMessage) {}
}
