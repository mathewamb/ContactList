package com.example.contactlist.model.utility



fun String.formatPhone(): String =
    if (this.length == 10) "(${substring(0,3)}) ${substring(3,6)}-${substring(6)}" else this

