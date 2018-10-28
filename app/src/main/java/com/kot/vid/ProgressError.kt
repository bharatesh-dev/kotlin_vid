package com.kot.vid

import android.databinding.ObservableBoolean
import android.databinding.ObservableField

data class ProgressError(var error: ObservableField<String>, var progress: ObservableBoolean)
