package org.kie.workbench.common.widgets.client.datamodel;

import java.net.URL;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.drools.workbench.models.datamodel.oracle.PackageDataModelOracle;
import org.drools.workbench.models.datamodel.oracle.TypeSource;
import org.jboss.weld.environment.se.StartMain;
import org.junit.Before;
import org.junit.Test;
import org.kie.commons.java.nio.fs.file.SimpleFileSystemProvider;
import org.kie.workbench.common.services.datamodel.backend.server.service.DataModelService;
import org.kie.workbench.common.services.datamodel.model.PackageDataModelOracleBaselinePayload;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for DataModelService
 */
public class PackageDataModelDeclaredTypesTests {

    private final SimpleFileSystemProvider fs = new SimpleFileSystemProvider();
    private BeanManager beanManager;
    private Paths paths;

    @Before
    public void setUp() throws Exception {
        //Bootstrap WELD container
        StartMain startMain = new StartMain( new String[ 0 ] );
        beanManager = startMain.go().getBeanManager();

        //Instantiate Paths used in tests for Path conversion
        final Bean pathsBean = (Bean) beanManager.getBeans( Paths.class ).iterator().next();
        final CreationalContext cc = beanManager.createCreationalContext( pathsBean );
        paths = (Paths) beanManager.getReference( pathsBean,
                                                  Paths.class,
                                                  cc );

        //Ensure URLs use the default:// scheme
        fs.forceAsDefault();
    }

    @Test
    public void testPackageDeclaredTypes() throws Exception {
        final Bean dataModelServiceBean = (Bean) beanManager.getBeans( DataModelService.class ).iterator().next();
        final CreationalContext cc = beanManager.createCreationalContext( dataModelServiceBean );
        final DataModelService dataModelService = (DataModelService) beanManager.getReference( dataModelServiceBean,
                                                                                               DataModelService.class,
                                                                                               cc );

        final URL packageUrl = this.getClass().getResource( "/DataModelBackendDeclaredTypesTest1/src/main/java/t1p1" );
        final org.kie.commons.java.nio.file.Path nioPackagePath = fs.getPath( packageUrl.toURI() );
        final Path packagePath = paths.convert( nioPackagePath );

        final PackageDataModelOracle projectLoader = dataModelService.getDataModel( packagePath );

        //Emulate server-to-client conversions
        final AsyncPackageDataModelOracle oracle = new AsyncPackageDataModelOracleImpl();
        final PackageDataModelOracleBaselinePayload dataModel = new PackageDataModelOracleBaselinePayload();
        dataModel.setModelFields( projectLoader.getProjectModelFields() );
        PackageDataModelOracleTestUtils.populateDataModelOracle( mock( Path.class ),
                                                                 new MockHasImports(),
                                                                 oracle,
                                                                 dataModel );

        assertNotNull( dataModel );

        assertEquals( 2,
                      oracle.getFactTypes().length );
        PackageDataModelOracleTestUtils.assertContains( "Bean1",
                                                        oracle.getFactTypes() );
        PackageDataModelOracleTestUtils.assertContains( "DRLBean",
                                                        oracle.getFactTypes() );

        assertEquals( 1,
                      oracle.getExternalFactTypes().length );
        PackageDataModelOracleTestUtils.assertContains( "t1p2.Bean2",
                                                        oracle.getExternalFactTypes() );

        assertEquals( TypeSource.JAVA_PROJECT,
                      oracle.getTypeSource( "Bean1" ) );
        assertEquals( TypeSource.DECLARED,
                      oracle.getTypeSource( "DRLBean" ) );
        assertEquals( TypeSource.JAVA_PROJECT,
                      oracle.getTypeSource( "t1p2.Bean2" ) );
    }

}

