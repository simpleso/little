package top.andnux.little.core.http

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface IHttpService {

    @GET
    fun get(
        @Url url: String
    ): Call<ResponseBody>

    @POST
    fun postRow(
        @Url url: String,
        @Body requestBody: RequestBody,
        @HeaderMap map: Map<String, String>
    ): Call<ResponseBody>

    @POST
    @FormUrlEncoded
    fun post(
        @Url url: String,
        @FieldMap fieldMap: Map<String, Any>,
        @HeaderMap header: Map<String, String>
    ): Call<ResponseBody>

    @PUT
    fun putRow(
        @Url url: String,
        @Body requestBody: RequestBody,
        @HeaderMap header: Map<String, String>
    ): Call<ResponseBody>

    @PUT
    @FormUrlEncoded
    fun put(
        @Url url: String,
        @FieldMap fieldMap: Map<String, Any>,
        @HeaderMap header: Map<String, String>
    ): Call<ResponseBody>


    @DELETE
    fun deleteRow(
        @Url url: String,
        @Body requestBody: RequestBody,
        @HeaderMap header: Map<String, String>
    ): Call<ResponseBody>

    @DELETE
    @FormUrlEncoded
    fun delete(
        @Url url: String,
        @FieldMap fieldMap: Map<String, Any>,
        @HeaderMap header: Map<String, String>
    ): Call<ResponseBody>

    @POST
    @Multipart
    fun upload(
        @Url url: String,
        @FieldMap fieldMap: Map<String, Any>,
        @PartMap partMap: Map<String, RequestBody>,
        @HeaderMap header: Map<String, String>
    ): Call<ResponseBody>
}