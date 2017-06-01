/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.os890.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.os890.cdi.template.model.ConfigEntry;
import org.os890.cdi.template.persistence.ConfigRepository;

import dnl.utils.text.table.TextTable;

@RunWith(CdiTestRunner.class)
public class SimpleRepositoryTest {
    @Inject
    private ConfigRepository configRepository;

    @Inject
    private EntityManager em;

    private static Map< String, Double > times = new HashMap< String, Double >();

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
        for( int i = 10; i <= 10000; i *= 10 )
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

    @Before
    public void warmUp()
    {
        System.out.println( "warmUp" );
        final long endTime, startTime = System.nanoTime();
        for( int i = 0; i < 100; i++ )
        {
            ConfigEntry configEntry = new ConfigEntry();
            configEntry.setEntryKey( "test-key-warmup" + i );
            configEntry.setValue( "test-value-warmup" + i );
            configRepository.save( configEntry );
        }
        configRepository.findAll().forEach( configRepository::remove );
        endTime = System.nanoTime();
        System.out.println( "warm up " + (endTime - startTime) / 1_000_000_000. + " sec" );
    }

    @Test
    public void simpleCaseDS10()
    {
        simpleCaseDS( 10 );
    }

    @Test
    public void simpleCaseDS100()
    {
        simpleCaseDS( 100 );
    }

    @Test
    public void simpleCaseDS1000()
    {
        simpleCaseDS( 1000 );
    }

    @Test
    public void simpleCaseDS10000()
    {
        simpleCaseDS( 10000 );
    }

    public void simpleCaseDS( int c )
    {
        long endTime, startTime = System.nanoTime();
        for( int i = 0; i < c; i++ )
        {
            ConfigEntry configEntry = new ConfigEntry();
            configEntry.setEntryKey( "test-key" + i );
            configEntry.setValue( "test-value" + i );
            configRepository.save( configEntry );
        }
        endTime = System.nanoTime();
        System.out.println( "save " + c + " ds took " + (endTime - startTime) / 1_000_000_000. + " sec" );
        times.put( "ds " + c, (endTime - startTime) / 1_000_000_000. );

    }

    @Test
    public void simpleCaseEM10()
    {
        simpleCaseEM( 10 );
    }

    @Test
    public void simpleCaseEM100()
    {
        simpleCaseEM( 100 );
    }

    @Test
    public void simpleCaseEM1000()
    {
        simpleCaseEM( 1000 );
    }

    @Test
    public void simpleCaseEM10000()
    {
        simpleCaseEM( 10000 );
    }

    public void simpleCaseEM( int c )
    {
        long endTime, startTime = System.nanoTime();
        for( int i = 0; i < c; i++ )
        {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            ConfigEntry configEntry = new ConfigEntry();
            configEntry.setEntryKey( "test-key" + i );
            configEntry.setValue( "test-value" + i );
            em.persist( configEntry );
            em.flush();
            tx.commit();
        }
        endTime = System.nanoTime();
        System.out.println( "save " + c + " em took " + (endTime - startTime) / 1_000_000_000. + " sec" );
        times.put( "em " + c, (endTime - startTime) / 1_000_000_000. );
    }
}
