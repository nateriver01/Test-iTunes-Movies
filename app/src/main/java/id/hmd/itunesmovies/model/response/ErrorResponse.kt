package id.hmd.itunesmovies.model.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ErrorResponse(
	val queryParameters: QueryParameters? = null,
	val errorMessage: String? = null
) : Parcelable

@Parcelize
data class QueryParameters(
	val output: String? = null,
	val country: String? = null,
	val limit: String? = null,
	val callback: String? = null,
	val term: String? = null,
	val lang: String? = null
) : Parcelable
