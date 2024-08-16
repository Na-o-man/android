package com.hgh.na_o_man.presentation.ui.detail.GroupDetailFolder

import CloudBtn
import SmallCloudBtn
import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.hgh.na_o_man.R
import com.hgh.na_o_man.domain.model.Dummy
import com.hgh.na_o_man.domain.model.FolderDummy
import com.hgh.na_o_man.presentation.base.LoadState
import com.hgh.na_o_man.presentation.component.EndTopCloud
import com.hgh.na_o_man.presentation.component.FolderProfile
import com.hgh.na_o_man.presentation.component.StartEndAppBar
import com.hgh.na_o_man.presentation.component.StateErrorScreen
import com.hgh.na_o_man.presentation.component.StateLoadingScreen
import com.hgh.na_o_man.presentation.component.groupdetail.Bigfolder
import com.hgh.na_o_man.presentation.component.groupdetail.GroupInfo
import com.hgh.na_o_man.presentation.component.groupdetail.SmallFolder
import com.hgh.na_o_man.presentation.ui.detail.ALL_PHOTO_ID
import com.hgh.na_o_man.presentation.ui.detail.GroupDetailActivity.Companion.GROUP_DETAIL


@ExperimentalPagerApi
@Composable
fun GroupDetailFolderScreen(
    navigationMyPage: (Long) -> Unit,
    navigationPhotoList: (Long, Long, Long) -> Unit,
    navigationVote: (Long) -> Unit,
    navigationBack: () -> Unit,
    navController: NavHostController = rememberNavController(),
    viewModel: GroupDetailFolderViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()
    val context = LocalContext.current as Activity
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris: List<Uri> ->
        viewModel.uploadUri(uris)
    }

    val groupId = remember { context.intent.getLongExtra(GROUP_DETAIL, 0L) }

    LaunchedEffect(groupId) {
        viewModel.initGroupId(groupId)
    }

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is GroupDetailFolderContract.GroupDetailFolderSideEffect.NaviPhotoList -> {
                    navigationPhotoList(effect.groupId, effect.profiledId ,effect.memberId)
                }

                is GroupDetailFolderContract.GroupDetailFolderSideEffect.NaviVote -> {
                    navigationVote(effect.groupId)
                }

                is GroupDetailFolderContract.GroupDetailFolderSideEffect.NaviPhotoPicker -> {
                    imagePickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                    )
                }

                else -> Unit
            }
        }
    }


    Log.d("리컴포저블", "GroupDetailScreen")

    val scope = rememberCoroutineScope()

    // 페이저용
    val pagerState = rememberPagerState()

//    val pagerState = rememberPagerState(initialPage = {dummyFolderData.size})
//    val selectedTabIndex = remember{ derivedStateOf { pagerState.currentPage }  }

    when (viewState.loadState) {
        LoadState.LOADING -> {
            StateLoadingScreen()
        }

        LoadState.ERROR -> {
            StateErrorScreen()
        }

        LoadState.SUCCESS -> {
            Scaffold(
                topBar = {
                    StartEndAppBar(
                        onStartClick = {
                            navigationBack()
                        },
                        onEndClick = {
                            navigationMyPage(viewState.groupId)
                        }
                    )
                },
                containerColor = Color(0xFFBBCFE5)

            ) { padding ->
                Box(modifier = Modifier.fillMaxSize()) {
                    EndTopCloud()
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.height(100.dp))

                        Box(
                            modifier = Modifier.padding(start = 200.dp)
                        ) {
                            viewState.groupDetail?.let { groupDetail ->
                                GroupInfo(
                                    title = groupDetail.name,
                                    participantCount = groupDetail.memberCount,
                                    date = groupDetail.createdAt
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        viewState.groupDetail?.let { groupDetail ->
                            val bigFolderCount = groupDetail.profileInfoList.size + 2
                            val itemCount = groupDetail.memberCount + 2
                            HorizontalPager(
                                count = bigFolderCount,
                                state = pagerState,
                                contentPadding = PaddingValues(horizontal = 40.dp), // Set padding to create partial view
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                            )
                            { page ->
                                val scale by animateFloatAsState(
                                    targetValue = 1.1f
                                )
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .scale(scale)
                                ) {
                                    val folderInfo = if (page < groupDetail.profileInfoList.size) {
                                        FolderDummy(
                                            imageRes = groupDetail.profileInfoList[page].image,
                                            name = groupDetail.profileInfoList[page].name
                                        )
                                    } else {
                                        when (page - groupDetail.profileInfoList.size) {
                                            0 -> FolderDummy(
                                                imageRes = R.drawable.ic_example.toString(),
                                                name = "others"
                                            )
                                            1 -> FolderDummy(
                                                imageRes = R.drawable.ic_example.toString(),
                                                name = "all"
                                            )
                                            else -> FolderDummy(
                                                imageRes = R.drawable.ic_example.toString(),
                                                name = "알 수 없음"
                                            )
                                        }
                                    }

                                    val memberId = if (page < groupDetail.profileInfoList.size) groupDetail.profileInfoList[page].memberId else null
                                    if (memberId != null && memberId>0) {
                                        Bigfolder(
                                            folderInfo = folderInfo,
                                            onClick = {
                                                navigationPhotoList(
                                                    groupDetail.shareGroupId,
                                                    groupDetail.profileInfoList[page].profileId,
                                                    groupDetail.profileInfoList[page].memberId
                                                )
                                            }
                                        )
                                    } else {
                                        if(folderInfo.name == "others" || folderInfo.name == "all") {
                                            Bigfolder(
                                                folderInfo = folderInfo,
                                                onClick = {
                                                    navigationPhotoList(
                                                        groupDetail.shareGroupId,
                                                        groupDetail.profileInfoList[page].profileId,
                                                        groupDetail.profileInfoList[page].memberId
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                            HorizontalPagerIndicator(
                                pagerState = pagerState,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(16.dp),
                                pageCount = bigFolderCount
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .offset(y = 90.dp)
                        ) {
                            SmallCloudBtn(title = "이미지\n분류") {
                                viewModel.setEvent(
                                    GroupDetailFolderContract.GroupDetailFolderEvent.OnUploadClicked
                                )
                            }
                            CloudBtn(title = "다운로드") {
                                // 테스트용 - 삭제해야함
                                viewModel.setEvent(
                                    GroupDetailFolderContract.GroupDetailFolderEvent.OnUserFolderClicked(
                                        profileId = ALL_PHOTO_ID,
                                        memberId = -1L,
                                    )
                                )
                            }
                            SmallCloudBtn(title = "지난 안건") {
                                viewModel.setEvent(
                                    GroupDetailFolderContract.GroupDetailFolderEvent.OnVoteClicked
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
fun PreView(
) {
    GroupDetailFolderScreen(
        navigationMyPage = {},
        navigationPhotoList = { _, _, _ -> },
        navigationVote = { _ -> },
        navigationBack = {})
}