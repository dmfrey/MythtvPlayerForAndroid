package org.mythtv.android.presentation.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author dmfrey
 *
 * Created on 2/1/17.
 *
 */

public class UtilsTest {

    @Test
    public void testMeetsMinimumVersion() throws Exception {

        assertThat( Utils.meetsMinimumVersion( "b6ae90c071", 0.28f ) ).isEqualTo( false );
        assertThat( Utils.meetsMinimumVersion( "Unknown", 0.28f ) ).isEqualTo( false );
        assertThat( Utils.meetsMinimumVersion( "v0.27.6", 0.28f ) ).isEqualTo( false );
        assertThat( Utils.meetsMinimumVersion( "v0.27", 0.28f ) ).isEqualTo( false );
        assertThat( Utils.meetsMinimumVersion( "0.27", 0.28f ) ).isEqualTo( false );
        assertThat( Utils.meetsMinimumVersion( "v0.27-xyz", 0.28f ) ).isEqualTo( false );
        assertThat( Utils.meetsMinimumVersion( "v0.26", 0.28f ) ).isEqualTo( false );
        assertThat( Utils.meetsMinimumVersion( "0.26", 0.28f ) ).isEqualTo( false );
        assertThat( Utils.meetsMinimumVersion( "v0.26-xyz", 0.28f ) ).isEqualTo( false );

        assertThat( Utils.meetsMinimumVersion( "v0.28", 0.28f ) ).isEqualTo( true );
        assertThat( Utils.meetsMinimumVersion( "0.28", 0.28f ) ).isEqualTo( true );
        assertThat( Utils.meetsMinimumVersion( "v0.28-xyz", 0.28f ) ).isEqualTo( true );
        assertThat( Utils.meetsMinimumVersion( "v28.0", 0.28f ) ).isEqualTo( true );

        assertThat( Utils.meetsMinimumVersion( "v0.29", 0.28f ) ).isEqualTo( true );
        assertThat( Utils.meetsMinimumVersion( "0.29", 0.28f ) ).isEqualTo( true );
        assertThat( Utils.meetsMinimumVersion( "v0.29-xyz", 0.28f ) ).isEqualTo( true );
        assertThat( Utils.meetsMinimumVersion( "v29.0", 0.28f ) ).isEqualTo( true );

        assertThat( Utils.meetsMinimumVersion( "v0.28.1-21-ge26a33c", 0.28f ) ).isEqualTo( true );

    }

}
