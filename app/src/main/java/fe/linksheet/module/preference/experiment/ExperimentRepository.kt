package fe.linksheet.module.preference.experiment

import android.content.Context

class ExperimentRepository(val context: Context) : FlowPreferenceRepository(context, "experiments") {

    fun hasExperiment(keys: Set<String>): Boolean {
        for (key in keys) {
            if (hasStoredValue(key)) return true
        }
        return false
    }
}
