package io.github.fededri.arch

import kotlin.native.concurrent.freeze

internal actual fun <T> T.freeze(): T = freeze()