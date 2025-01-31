package com.hgh.na_o_man.data.source.remote.api

import com.hgh.na_o_man.data.dto.ApiResult
import com.hgh.na_o_man.data.dto.share_group.request.GroupAddRequestDto
import com.hgh.na_o_man.data.dto.share_group.request.GroupJoinRequestDto
import com.hgh.na_o_man.data.dto.share_group.response.GroupAddDto
import com.hgh.na_o_man.data.dto.share_group.response.GroupJoinDto
import com.hgh.na_o_man.data.dto.share_group.response.GroupListReferDto
import com.hgh.na_o_man.data.dto.share_group.response.CheckSpecificGroupDto
import com.hgh.na_o_man.data.dto.share_group.response.GroupInviteDto
import com.hgh.na_o_man.data.dto.share_group.response.ShareGroupNameListDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ShareGroupsService {
    @GET("shareGroups")
    suspend fun getInvite(
        @Query("inviteCode") inviteCode: String,
    ) : Response<ApiResult<GroupInviteDto>>

    @POST("shareGroups")
    suspend fun postAddGroup(
        @Body addGroupAddRequest: GroupAddRequestDto
    ) : Response<ApiResult<GroupAddDto>>

    @POST("shareGroups/join")
    suspend fun postJoinGroup(
        @Body joinGroupJoinRequest: GroupJoinRequestDto
    ) : Response<ApiResult<GroupJoinDto>>

    @GET("shareGroups/{shareGroupId}")
    suspend fun groupSearch(
        @Path("shareGroupId") shareGroupId : Long // 맞는지 확인해야 함
    ) : Response<ApiResult<GroupAddDto>> // 재활용 가능한지 확인

    @GET("shareGroups/{shareGroupId}")
    suspend fun checkSpecificGroup(
        @Path("shareGroupId") shareGroupId: Long
    ) : Response<ApiResult<CheckSpecificGroupDto>>

    @GET("shareGroups/my")
    suspend fun groupListRefer(
        @Query("page") page : Int,
        @Query("size") size : Int
    ) : Response<ApiResult<GroupListReferDto>>

    @GET("shareGroups/my/shareGroupNameList")
    suspend fun shareGroupNameList(
        @Query("page") page : Int,
        @Query("size") size : Int
    ) : Response<ApiResult<ShareGroupNameListDto>>
}