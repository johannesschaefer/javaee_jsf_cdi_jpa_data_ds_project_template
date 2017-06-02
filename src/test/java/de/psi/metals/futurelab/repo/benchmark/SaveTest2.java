package de.psi.metals.futurelab.repo.benchmark;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.psi.metals.futurelab.repo.benchmark.repo.MaterialRepoIf;

@RunWith(CdiTestRunner.class)
public class SaveTest2 {
    private static Map<String, Long> times = new HashMap<>();

    @Inject
    private MaterialRepoIf matRepo;

    @Inject
    private EntityManager entityManager;

    @AfterClass
    public static void afterClass() {
        String csv = "";
        String csvEM = "";
        String csvDS = "";
        for (int i = 10; i <= 2560; i *= 2) {
            csv += i + "\n";
            csvEM += times.get("em " + i) + "\n";
            csvDS += times.get("ds " + i) + "\n";
        }

        System.out.println(csv);
        System.out.println("*plain*");
        System.out.println(csvEM);
        System.out.println("*DS*");
        System.out.println(csvDS);

    }

    @Before
    public void warmUp() {
        System.out.println("warmUp");
        final long endTime, startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Material mat = new Material();
            mat.setMaterialName("mat-ds" + i);
            mat.setGrade("AAA");
            matRepo.save(mat);
        }
        matRepo.findAll().forEach(matRepo::remove);
        endTime = System.currentTimeMillis();
        long result = endTime - startTime;
        System.out.println("warm up " + result + " msec");
    }

    @Test
    public void saveMaterialDeltaSpike10() {
        ds(10);
    }

    @Test
    public void saveMaterialDeltaSpike20() {
        ds(20);
    }

    @Test
    public void saveMaterialDeltaSpike40() {
        ds(40);
    }

    @Test
    public void saveMaterialDeltaSpike80() {
        ds(80);
    }

    @Test
    public void saveMaterialDeltaSpike160() {
        ds(160);
    }

    @Test
    public void saveMaterialDeltaSpike320() {
        ds(320);
    }

    @Test
    public void saveMaterialDeltaSpike640() {
        ds(640);
    }

    @Test
    public void saveMaterialDeltaSpike1280() {
        ds(1280);
    }

    @Test
    public void saveMaterialDeltaSpike2560() {
        ds(2560);
    }

    private void ds(int c) {
        System.out.println("saveMaterialDeltaSpike");
        // save
        long endTime, startTime = System.currentTimeMillis();
        for (int i = 0; i < c; i++) {
            Material mat = new Material();
            mat.setMaterialName("mat-ds" + i);
            mat.setGrade("AAA");
            matRepo.save( mat );
        }
        endTime = System.currentTimeMillis();
        long result = endTime - startTime;
        System.out.println("save " + c + " ds took " + result + " msec");
        times.put("ds " + c, result);
    }

    @Test
    public void saveMaterialEntityManager10() {
        em(10);
    }

    @Test
    public void saveMaterialEntityManager20() {
        em(20);
    }

    @Test
    public void saveMaterialEntityManager40() {
        em(40);
    }

    @Test
    public void saveMaterialEntityManager80() {
        em(80);
    }

    @Test
    public void saveMaterialEntityManager160() {
        em(160);
    }

    @Test
    public void saveMaterialEntityManager320() {
        em(320);
    }

    @Test
    public void saveMaterialEntityManager640() {
        em(640);
    }

    @Test
    public void saveMaterialEntityManager1280() {
        em(1280);
    }

    @Test
    public void saveMaterialEntityManager2560() {
        em(2560);
    }

    private void em(int c) {
        System.out.println("saveMaterialEntityManager");
        long endTime, startTime = System.currentTimeMillis();
        for (int i = 0; i < c; i++) {
            EntityTransaction tx = entityManager.getTransaction();
            tx.begin();
            Material mat = new Material();
            mat.setMaterialName("mat-em" + i);
            mat.setGrade("AAA");
            entityManager.persist(mat);
            entityManager.flush();
            tx.commit();
        }
        endTime = System.currentTimeMillis();
        long result = endTime - startTime;
        System.out.println("save " + c + " em took " + result + " msec");
        times.put("em " + c, result);
    }
}