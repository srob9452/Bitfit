package com.codepath.bitfit_part1;

import android.app.Application

class BitFitApplication : Application() {
    val db by lazy { AppDatabase.getInstance(this) }
}
