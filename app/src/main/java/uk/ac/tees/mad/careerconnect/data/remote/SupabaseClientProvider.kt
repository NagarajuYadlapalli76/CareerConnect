package uk.ac.tees.mad.careerconnect.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.storage.Storage


object SupabaseClientProvider {
    val client =
        createSupabaseClient(
            supabaseUrl = "https://apugiylcjwsglkzeojws.supabase.co",
            supabaseKey = "sb_publishable_axkXBSV0Dlu5LkrcO6DnpQ_k_smPJgi"
        ) {
            install(GoTrue)
            install(Storage)
        }


}
