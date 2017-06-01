//******************************************************************
//                                                                 
//  SaveTest.java                                               
//  Copyright 2017 PSI AG. All rights reserved.              
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//                                                                 
// ******************************************************************

package de.psi.metals.futurelab.repo.benchmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.psi.metals.futurelab.repo.benchmark.repo.MaterialRepoIf;
import dnl.utils.text.table.TextTable;

@RunWith( CdiTestRunner.class )
public class SaveTest
{
    @Inject
    private MaterialRepoIf matRepo;

    private static Map< String, Double > times = new HashMap<>();

    @Inject
    private EntityManager em;

    @AfterClass
    public static void afterClass()
    {
        String csv = "";
        String csvEM = "";
        String csvDS = "";
        List< String > cols = new ArrayList<>();
        cols.add( "" );
        List< String > vDS = new ArrayList<>();
        vDS.add( "DS" );
        List< String > vEM = new ArrayList<>();
        vEM.add( "EM" );
        for( int i = 10; i <= 10240; i *= 2 )
        {
            cols.add( "iter " + i );
            csv += i + ";";
            csvEM += times.get( "em " + i ) + ";";
            vEM.add( times.get( "em " + i ).toString() );
            csvDS += times.get( "ds " + i ) + ";";
            vDS.add( times.get( "ds " + i ).toString() );
        }

        System.out.println( csv );
        System.out.println( csvEM );
        System.out.println( csvDS );

        TextTable tt = new TextTable( cols.toArray( new String[ 0 ] ), new String[][]
        { vDS.toArray( new String[ 0 ] ), vEM.toArray( new String[ 0 ] ) } );
        tt.printTable();
    }

    @ApplicationScoped
    @Before
    public void warmUp()
    {
        System.out.println( "warmUp" );
        final long endTime, startTime = System.nanoTime();
        for( int i = 0; i < 100; i++ )
        {
            Material mat = new Material();
            mat.setMaterialName( "mat-ds" + i );
            mat.setGrade( "AAA" );
            matRepo.save( mat );
        }
        matRepo.findAll().forEach( matRepo::remove );
        endTime = System.nanoTime();
        System.out.println( "warm up " + (endTime - startTime) / 10000000000. + " sec" );
    }

    @ApplicationScoped
    @Test
    public void saveMaterialDeltaSpike10()
    {
        ds( 10 );
    }

    @ApplicationScoped
    @Test
    public void saveMaterialDeltaSpike20()
    {
        ds( 20 );
    }

    @ApplicationScoped
    @Test
    public void saveMaterialDeltaSpike40()
    {
        ds( 40 );
    }

    @ApplicationScoped
    @Test
    public void saveMaterialDeltaSpike80()
    {
        ds( 80 );
    }

    @ApplicationScoped
    @Test
    public void saveMaterialDeltaSpike160()
    {
        ds( 160 );
    }

    @ApplicationScoped
    @Test
    public void saveMaterialDeltaSpike320()
    {
        ds( 320 );
    }

    @ApplicationScoped
    @Test
    public void saveMaterialDeltaSpike640()
    {
        ds( 640 );
    }

    @ApplicationScoped
    @Test
    public void saveMaterialDeltaSpike1280()
    {
        ds( 1280 );
    }

    @ApplicationScoped
    @Test
    public void saveMaterialDeltaSpike2560()
    {
        ds( 2560 );
    }

    @ApplicationScoped
    @Test
    public void saveMaterialDeltaSpike5120()
    {
        ds( 5120 );
    }

    @ApplicationScoped
    @Test
    public void saveMaterialDeltaSpike10240()
    {
        ds( 10240 );
    }

    private void ds( int c )
    {
        System.out.println( "saveMaterialDeltaSpike" );
        // save
        long endTime, startTime = System.nanoTime();
        for( int i = 0; i < c; i++ )
        {
            Material mat = new Material();
            mat.setMaterialName( "mat-ds" + i );
            mat.setGrade( "AAA" );
            matRepo.save( mat );
        }
        endTime = System.nanoTime();
        System.out.println( "save " + c + " ds took " + (endTime - startTime) / 1000000000. + " sec" );
        times.put( "ds " + c, (endTime - startTime) / 1000000000. );
    }

    @Test
    public void saveMaterialEntityManager10()
    {
        em( 10 );
    }

    @Test
    public void saveMaterialEntityManager20()
    {
        em( 20 );
    }

    @Test
    public void saveMaterialEntityManager40()
    {
        em( 40 );
    }

    @Test
    public void saveMaterialEntityManager80()
    {
        em( 80 );
    }

    @Test
    public void saveMaterialEntityManager160()
    {
        em( 160 );
    }

    @Test
    public void saveMaterialEntityManager320()
    {
        em( 320 );
    }

    @Test
    public void saveMaterialEntityManager640()
    {
        em( 640 );
    }

    @Test
    public void saveMaterialEntityManager1280()
    {
        em( 1280 );
    }

    @Test
    public void saveMaterialEntityManager2560()
    {
        em( 2560 );
    }

    @Test
    public void saveMaterialEntityManager5120()
    {
        em( 5120 );
    }

    @Test
    public void saveMaterialEntityManager10240()
    {
        em( 10240 );
    }

    private void em( int c )
    {
        System.out.println( "saveMaterialEntityManager" );
        long endTime, startTime = System.nanoTime();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        for( int i = 0; i < c; i++ )
        {
            Material mat = new Material();
            mat.setMaterialName( "mat-em" + i );
            mat.setGrade( "AAA" );
            em.persist( mat );
        }
        tx.commit();
        endTime = System.nanoTime();
        System.out.println( "save " + c + " em took " + (endTime - startTime) / 10000000000. + " sec" );
        times.put( "em " + c, (endTime - startTime) / 1000000000. );
    }
}
