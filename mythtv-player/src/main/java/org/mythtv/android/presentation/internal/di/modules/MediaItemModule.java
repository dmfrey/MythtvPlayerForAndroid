/*
 * MythtvPlayerForAndroid. An application for Android users to play MythTV Recordings and Videos
 * Copyright (c) 2016. Daniel Frey
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mythtv.android.presentation.internal.di.modules;

import org.mythtv.android.domain.Media;
import org.mythtv.android.domain.executor.PostExecutionThread;
import org.mythtv.android.domain.executor.ThreadExecutor;
import org.mythtv.android.domain.interactor.AddLiveStreamUseCase;
import org.mythtv.android.domain.interactor.DynamicUseCase;
import org.mythtv.android.domain.interactor.GetMediaItemDetails;
import org.mythtv.android.domain.interactor.PostUpdatedWatchedStatus;
import org.mythtv.android.domain.interactor.RemoveLiveStreamUseCase;
import org.mythtv.android.domain.interactor.UseCase;
import org.mythtv.android.domain.repository.MediaItemRepository;
import org.mythtv.android.presentation.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides mediaItem related collaborators.
 *
 * @author dmfrey
 *
 * Created on 8/26/15.
 */
@Module
public class MediaItemModule {

    private int id = -1;
    private Media media = null;

    public MediaItemModule() {
        // This constructor is intentionally empty. Nothing special is needed here.
    }

    public MediaItemModule( final int id, final Media media ) {

        this.id = id;
        this.media = media;

    }

    @Provides
    @PerActivity
    @Named( "mediaItemDetails" )
    UseCase provideGetMediaItemDetailsUseCase( MediaItemRepository mediaItemRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread ) {

        return new GetMediaItemDetails( media, id, mediaItemRepository, threadExecutor, postExecutionThread );
    }

    @Provides
    @PerActivity
    @Named( "addLiveStream" )
    UseCase provideAddLiveStreamUseCase( MediaItemRepository mediaItemRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread ) {

        return new AddLiveStreamUseCase( media, id, mediaItemRepository, threadExecutor, postExecutionThread );

    }

    @Provides
    @PerActivity
    @Named( "removeLiveStream" )
    UseCase provideRemoveLiveStreamUseCase( MediaItemRepository mediaItemRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread ) {

        return new RemoveLiveStreamUseCase( media, id, mediaItemRepository, threadExecutor, postExecutionThread );

    }

    @Provides
    @PerActivity
    @Named( "updateWatchedStatus" )
    DynamicUseCase provideUpdateWatchedStatusUseCase( MediaItemRepository mediaItemRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread ) {

        return new PostUpdatedWatchedStatus( media, id, mediaItemRepository, threadExecutor, postExecutionThread );

    }

}
