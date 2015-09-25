package org.mythtv.android.data.entity.mapper;

import org.junit.Before;
import org.junit.Test;
import org.mythtv.android.data.ApplicationTestCase;
import org.mythtv.android.data.entity.TitleInfoEntity;
import org.mythtv.android.domain.TitleInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by dmfrey on 9/18/15.
 */
public class TitleInfoEntityDataMapperTest extends ApplicationTestCase {

    private static final String FAKE_TITLE = "fake title";
    private static final String FAKE_INETREF = "fake inetref";
    private static final int FAKE_COUNT = 1;

    private TitleInfoEntityDataMapper titleInfoEntityDataMapper;

    @Before
    public void setup() throws Exception {

        titleInfoEntityDataMapper = new TitleInfoEntityDataMapper();

    }

    @Test
    public void testTransformTitleInfoEntity() {

        TitleInfoEntity titleInfoEntity = createFakeTitleInfoEntity();
        TitleInfo titleInfo = titleInfoEntityDataMapper.transform( titleInfoEntity );

        assertThat( titleInfo, is( instanceOf( TitleInfo.class ) ) );
        assertThat( titleInfo.getTitle(), is( FAKE_TITLE ) );
        assertThat( titleInfo.getInetref(), is( FAKE_INETREF ) );
        assertThat( titleInfo.getCount(), is( FAKE_COUNT ) );

    }

    @Test
    public void testTransformTitleInfoEntityCollection() {

        TitleInfoEntity mockTitleInfoEntityOne = mock( TitleInfoEntity.class );
        TitleInfoEntity mockTitleInfoEntityTwo = mock( TitleInfoEntity.class );

        List<TitleInfoEntity> titleInfoEntityList = new ArrayList<>( 5 );
        titleInfoEntityList.add( mockTitleInfoEntityOne );
        titleInfoEntityList.add( mockTitleInfoEntityTwo );

        Collection<TitleInfo> titleInfoCollection = titleInfoEntityDataMapper.transform( titleInfoEntityList );

        assertThat( titleInfoCollection.toArray()[ 0 ], is( instanceOf( TitleInfo.class ) ) );
        assertThat( titleInfoCollection.toArray()[ 1 ], is( instanceOf( TitleInfo.class ) ) );
        assertThat( titleInfoCollection.size(), is( 2 ) );

    }

    private TitleInfoEntity createFakeTitleInfoEntity() {

        TitleInfoEntity titleInfoEntity = new TitleInfoEntity();
        titleInfoEntity.setTitle( FAKE_TITLE );
        titleInfoEntity.setInetref( FAKE_INETREF );
        titleInfoEntity.setCount( FAKE_COUNT );

        return titleInfoEntity;
    }

}