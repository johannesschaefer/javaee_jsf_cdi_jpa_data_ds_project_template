//******************************************************************
//                                                                 
//  ReadTest.java                                               
//  Copyright 2017 PSI AG. All rights reserved.              
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//                                                                 
// ******************************************************************

package de.psi.metals.futurelab.repo.benchmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.psi.metals.futurelab.repo.benchmark.repo.MaterialRepoIf;
import dnl.utils.text.table.TextTable;

@RunWith( CdiTestRunner.class )
public class ReadTest
{
    private static final int MAX = 10000;

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

    @BeforeClass
    public static void createData()
    {
        System.out.println( "createData" );
        EntityManagerFactory emfl = Persistence.createEntityManagerFactory( "testPU" );
        EntityManager eml = emfl.createEntityManager();
        final long endTime, startTime = System.nanoTime();
        EntityTransaction tx = eml.getTransaction();
        tx.begin();

        Query query = eml.createQuery( "DELETE FROM Material" );
        query.executeUpdate();

        for( int i = 0; i < MAX; i++ )
        {
            Material mat = new Material();
            mat.setMaterialName( "mat" + i );
            mat.setGrade( "AAA" );
            eml.persist( mat );
            // eml.flush();
        }
        tx.commit();
        endTime = System.nanoTime();
        System.out.println( "warm up " + (endTime - startTime) / 1_000_000_000. + " sec" );
    }

    @Test
    public void readDeltaSpike10()
    {
        ds( 10 );
    }

    @Test
    public void readDeltaSpike20()
    {
        ds( 20 );
    }

    @Test
    public void readDeltaSpike40()
    {
        ds( 40 );
    }

    @Test
    public void readDeltaSpike80()
    {
        ds( 80 );
    }

    @Test
    public void readDeltaSpike160()
    {
        ds( 160 );
    }

    @Test
    public void readDeltaSpike320()
    {
        ds( 320 );
    }

    @Test
    public void readDeltaSpike640()
    {
        ds( 640 );
    }

    @Test
    public void readDeltaSpike1280()
    {
        ds( 1280 );
    }

    @Test
    public void readDeltaSpike2560()
    {
        ds( 2560 );
    }

    @Test
    public void readDeltaSpike5120()
    {
        ds( 5120 );
    }

    @Test
    public void readDeltaSpike10240()
    {
        ds( 10240 );
    }

    private void ds( int c )
    {
        System.out.println( "readDeltaSpike" );

        // find
        Random r = new Random();
        long endTime, startTime = System.nanoTime();
        for( int i = 0; i < c; i++ )
        {
            matRepo.findBy( "mat" + r.nextInt( MAX ) );
        }
        endTime = System.nanoTime();
        System.out.println( "find ds took " + (endTime - startTime) / 1_000_000_000. + " sec" );
        times.put( "ds " + c, (endTime - startTime) / 1_000_000_000. );
    }

    @Test
    public void readEntityManager10()
    {
        em( 10 );
    }

    @Test
    public void readEntityManager20()
    {
        em( 20 );
    }

    @Test
    public void readEntityManager40()
    {
        em( 40 );
    }

    @Test
    public void readEntityManager80()
    {
        em( 80 );
    }

    @Test
    public void readEntityManager160()
    {
        em( 160 );
    }

    @Test
    public void readEntityManager320()
    {
        em( 320 );
    }

    @Test
    public void readEntityManager640()
    {
        em( 640 );
    }

    @Test
    public void readEntityManager1280()
    {
        em( 1280 );
    }

    @Test
    public void readEntityManager2560()
    {
        em( 2560 );
    }

    @Test
    public void readEntityManager5120()
    {
        em( 5120 );
    }

    @Test
    public void readEntityManager10240()
    {
        em( 10240 );
    }

    private void em( int c )
    {
        System.out.println( "readEntityManager" );
        // find
        Random r = new Random();
        long endTime, startTime = System.nanoTime();
        for( int i = 0; i < c; i++ )
        {
            em.find( Material.class, "mat" + r.nextInt( MAX ) );
        }
        endTime = System.nanoTime();
        System.out.println( "find em took " + (endTime - startTime) / 1_000_000_000. + " sec" );
        times.put( "em " + c, (endTime - startTime) / 1_000_000_000. );
    }
}
