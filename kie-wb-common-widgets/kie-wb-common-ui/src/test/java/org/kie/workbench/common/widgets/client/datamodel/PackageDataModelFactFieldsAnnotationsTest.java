package org.kie.workbench.common.widgets.client.datamodel;

import java.util.Map;
import java.util.Set;

import org.drools.workbench.models.commons.backend.oracle.ProjectDataModelOracleImpl;
import org.drools.workbench.models.datamodel.oracle.Annotation;
import org.drools.workbench.models.datamodel.oracle.PackageDataModelOracle;
import org.drools.workbench.models.datamodel.oracle.TypeSource;
import org.junit.Test;
import org.kie.workbench.common.services.datamodel.backend.server.builder.packages.PackageDataModelOracleBuilder;
import org.kie.workbench.common.services.datamodel.backend.server.builder.projects.ClassFactBuilder;
import org.kie.workbench.common.services.datamodel.backend.server.builder.projects.ProjectDataModelOracleBuilder;
import org.kie.workbench.common.services.datamodel.model.PackageDataModelOracleBaselinePayload;
import org.kie.workbench.common.widgets.client.datamodel.testclasses.Product;
import org.kie.workbench.common.widgets.client.datamodel.testclasses.annotations.SmurfHouse;
import org.uberfire.backend.vfs.Path;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for Fact's annotations
 */
public class PackageDataModelFactFieldsAnnotationsTest {

    @Test
    public void testCorrectPackageDMOZeroAnnotationAttributes() throws Exception {
        //Build ProjectDMO
        final ProjectDataModelOracleBuilder projectBuilder = ProjectDataModelOracleBuilder.newProjectOracleBuilder();
        final ProjectDataModelOracleImpl projectLoader = new ProjectDataModelOracleImpl();

        final ClassFactBuilder cb = new ClassFactBuilder( projectBuilder,
                                                          Product.class,
                                                          false,
                                                          TypeSource.JAVA_PROJECT );
        cb.build( projectLoader );

        //Build PackageDMO
        final PackageDataModelOracleBuilder packageBuilder = PackageDataModelOracleBuilder.newPackageOracleBuilder( "org.kie.workbench.common.widgets.client.datamodel.testclasses" );
        packageBuilder.setProjectOracle( projectLoader );
        final PackageDataModelOracle packageLoader = packageBuilder.build();

        //Emulate server-to-client conversions
        final AsyncPackageDataModelOracle oracle = new AsyncPackageDataModelOracleImpl();
        final PackageDataModelOracleBaselinePayload dataModel = new PackageDataModelOracleBaselinePayload();
        dataModel.setPackageName( packageLoader.getPackageName() );
        dataModel.setModelFields( packageLoader.getProjectModelFields() );
        dataModel.setTypeAnnotations( packageLoader.getProjectTypeAnnotations() );
        dataModel.setTypeFieldsAnnotations( packageLoader.getProjectTypeFieldsAnnotations() );
        PackageDataModelOracleTestUtils.populateDataModelOracle( mock( Path.class ),
                                                                 new MockHasImports(),
                                                                 oracle,
                                                                 dataModel );

        assertEquals( 1,
                      oracle.getFactTypes().length );
        assertEquals( "Product",
                      oracle.getFactTypes()[ 0 ] );

        final Map<String, Set<Annotation>> fieldsAnnotations = oracle.getTypeFieldsAnnotations( "Product" );
        assertNotNull( fieldsAnnotations );
        assertEquals( 0,
                      fieldsAnnotations.size() );
    }

    @Test
    public void testCorrectPackageDMOAnnotationAttributes() throws Exception {
        //Build ProjectDMO
        final ProjectDataModelOracleBuilder projectBuilder = ProjectDataModelOracleBuilder.newProjectOracleBuilder();
        final ProjectDataModelOracleImpl projectLoader = new ProjectDataModelOracleImpl();

        final ClassFactBuilder cb = new ClassFactBuilder( projectBuilder,
                                                          SmurfHouse.class,
                                                          false,
                                                          TypeSource.JAVA_PROJECT );
        cb.build( projectLoader );

        //Build PackageDMO
        final PackageDataModelOracleBuilder packageBuilder = PackageDataModelOracleBuilder.newPackageOracleBuilder( "org.kie.workbench.common.widgets.client.datamodel.testclasses.annotations" );
        packageBuilder.setProjectOracle( projectLoader );
        final PackageDataModelOracle packageLoader = packageBuilder.build();

        //Emulate server-to-client conversions
        final AsyncPackageDataModelOracle oracle = new AsyncPackageDataModelOracleImpl();
        final PackageDataModelOracleBaselinePayload dataModel = new PackageDataModelOracleBaselinePayload();
        dataModel.setPackageName( packageLoader.getPackageName() );
        dataModel.setModelFields( packageLoader.getProjectModelFields() );
        dataModel.setTypeAnnotations( packageLoader.getProjectTypeAnnotations() );
        dataModel.setTypeFieldsAnnotations( packageLoader.getProjectTypeFieldsAnnotations() );
        PackageDataModelOracleTestUtils.populateDataModelOracle( mock( Path.class ),
                                                                 new MockHasImports(),
                                                                 oracle,
                                                                 dataModel );

        assertEquals( 1,
                      oracle.getFactTypes().length );
        assertEquals( "SmurfHouse",
                      oracle.getFactTypes()[ 0 ] );

        final Map<String, Set<Annotation>> fieldsAnnotations = oracle.getTypeFieldsAnnotations( "SmurfHouse" );
        assertNotNull( fieldsAnnotations );
        assertEquals( 2,
                      fieldsAnnotations.size() );

        assertTrue( fieldsAnnotations.containsKey( "occupant" ) );
        final Set<Annotation> occupantAnnotations = fieldsAnnotations.get( "occupant" );
        assertNotNull( occupantAnnotations );
        assertEquals( 1,
                      occupantAnnotations.size() );

        final Annotation annotation = occupantAnnotations.iterator().next();
        assertEquals( "org.kie.workbench.common.widgets.client.datamodel.testclasses.annotations.SmurfFieldDescriptor",
                      annotation.getQualifiedTypeName() );
        assertEquals( "blue",
                      annotation.getAttributes().get( "colour" ) );
        assertEquals( "M",
                      annotation.getAttributes().get( "gender" ) );
        assertEquals( "Brains",
                      annotation.getAttributes().get( "description" ) );

        assertTrue( fieldsAnnotations.containsKey( "positionedOccupant" ) );
        final Set<Annotation> posOccupantAnnotations = fieldsAnnotations.get( "positionedOccupant" );
        assertNotNull( posOccupantAnnotations );
        assertEquals( 1,
                      posOccupantAnnotations.size() );

        final Annotation annotation2 = posOccupantAnnotations.iterator().next();
        assertEquals( "org.kie.workbench.common.widgets.client.datamodel.testclasses.annotations.SmurfFieldPositionDescriptor",
                      annotation2.getQualifiedTypeName() );
        assertEquals( Integer.toString( 1 ),
                      annotation2.getAttributes().get( "value" ) );
    }

    @Test
    public void testIncorrectPackageDMOZeroAnnotationAttributes() throws Exception {
        //Build ProjectDMO
        final ProjectDataModelOracleBuilder projectBuilder = ProjectDataModelOracleBuilder.newProjectOracleBuilder();
        final ProjectDataModelOracleImpl projectLoader = new ProjectDataModelOracleImpl();

        final ClassFactBuilder cb = new ClassFactBuilder( projectBuilder,
                                                          Product.class,
                                                          false,
                                                          TypeSource.JAVA_PROJECT );
        cb.build( projectLoader );

        //Build PackageDMO. Defaults to defaultpkg
        final PackageDataModelOracleBuilder packageBuilder = PackageDataModelOracleBuilder.newPackageOracleBuilder();
        packageBuilder.setProjectOracle( projectLoader );
        final PackageDataModelOracle packageLoader = packageBuilder.build();

        //Emulate server-to-client conversions
        final AsyncPackageDataModelOracle oracle = new AsyncPackageDataModelOracleImpl();
        final PackageDataModelOracleBaselinePayload dataModel = new PackageDataModelOracleBaselinePayload();
        dataModel.setPackageName( packageLoader.getPackageName() );
        dataModel.setModelFields( packageLoader.getProjectModelFields() );
        dataModel.setTypeAnnotations( packageLoader.getProjectTypeAnnotations() );
        dataModel.setTypeFieldsAnnotations( packageLoader.getProjectTypeFieldsAnnotations() );
        PackageDataModelOracleTestUtils.populateDataModelOracle( mock( Path.class ),
                                                                 new MockHasImports(),
                                                                 oracle,
                                                                 dataModel );

        assertEquals( 0,
                      oracle.getFactTypes().length );

        final Map<String, Set<Annotation>> fieldsAnnotations = oracle.getTypeFieldsAnnotations( "Product" );
        assertNotNull( fieldsAnnotations );
        assertEquals( 0,
                      fieldsAnnotations.size() );
    }

    @Test
    public void testIncorrectPackageDMOAnnotationAttributes() throws Exception {
        //Build ProjectDMO
        final ProjectDataModelOracleBuilder projectBuilder = ProjectDataModelOracleBuilder.newProjectOracleBuilder();
        final ProjectDataModelOracleImpl projectLoader = new ProjectDataModelOracleImpl();

        final ClassFactBuilder cb = new ClassFactBuilder( projectBuilder,
                                                          SmurfHouse.class,
                                                          false,
                                                          TypeSource.JAVA_PROJECT );
        cb.build( projectLoader );

        //Build PackageDMO. Defaults to defaultpkg
        final PackageDataModelOracleBuilder packageBuilder = PackageDataModelOracleBuilder.newPackageOracleBuilder();
        packageBuilder.setProjectOracle( projectLoader );
        final PackageDataModelOracle packageLoader = packageBuilder.build();

        //Emulate server-to-client conversions
        final AsyncPackageDataModelOracle oracle = new AsyncPackageDataModelOracleImpl();
        final PackageDataModelOracleBaselinePayload dataModel = new PackageDataModelOracleBaselinePayload();
        dataModel.setPackageName( packageLoader.getPackageName() );
        dataModel.setModelFields( packageLoader.getProjectModelFields() );
        dataModel.setTypeAnnotations( packageLoader.getProjectTypeAnnotations() );
        dataModel.setTypeFieldsAnnotations( packageLoader.getProjectTypeFieldsAnnotations() );
        PackageDataModelOracleTestUtils.populateDataModelOracle( mock( Path.class ),
                                                                 new MockHasImports(),
                                                                 oracle,
                                                                 dataModel );

        assertEquals( 0,
                      oracle.getFactTypes().length );

        final Map<String, Set<Annotation>> fieldAnnotations = oracle.getTypeFieldsAnnotations( "SmurfHouse" );
        assertNotNull( fieldAnnotations );
        assertEquals( 0,
                      fieldAnnotations.size() );
    }

}
