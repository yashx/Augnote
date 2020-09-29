package com.github.yashx.augnote

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<SqlDriver> { AndroidSqliteDriver(Database.Schema, androidContext(), "augnote.db") }
    single { Database(get()).augnoteQueries }
}