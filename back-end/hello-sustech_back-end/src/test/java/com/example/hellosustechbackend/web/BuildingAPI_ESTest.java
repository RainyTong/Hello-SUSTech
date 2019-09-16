/*
 * This file was automatically generated by EvoSuite
 * Fri May 24 15:26:33 GMT 2019
 */

package com.example.hellosustechbackend.web;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import com.example.hellosustechbackend.domain.Building;
import com.example.hellosustechbackend.service.BuildingService;
import com.example.hellosustechbackend.status.Status;
import com.example.hellosustechbackend.web.BuildingAPI;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.javaee.injection.Injector;
import org.junit.runner.RunWith;


public class BuildingAPI_ESTest {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      BuildingAPI buildingAPI0 = new BuildingAPI();
      BuildingService buildingService0 = mock(BuildingService.class, new ViolatedAssumptionAnswer());
      doReturn((Status) null).when(buildingService0).saveBuilding(any(Building.class));
      Injector.inject(buildingAPI0, (Class<?>) BuildingAPI.class, "buildingService", (Object) buildingService0);
      Injector.validateBean(buildingAPI0, (Class<?>) BuildingAPI.class);
      Status status0 = buildingAPI0.saveBuilding((Building) null);
      assertNull(status0);
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      BuildingAPI buildingAPI0 = new BuildingAPI();
      BuildingService buildingService0 = mock(BuildingService.class, new ViolatedAssumptionAnswer());
      doReturn((Building) null).when(buildingService0).getBuilding(anyString());
      Injector.inject(buildingAPI0, (Class<?>) BuildingAPI.class, "buildingService", (Object) buildingService0);
      Injector.validateBean(buildingAPI0, (Class<?>) BuildingAPI.class);
      Building building0 = buildingAPI0.getBuilding("");
      assertNull(building0);
  }

  @Test(timeout = 4000)
  public void test2()  throws Throwable  {
      BuildingAPI buildingAPI0 = new BuildingAPI();
      BuildingService buildingService0 = mock(BuildingService.class, new ViolatedAssumptionAnswer());
      doReturn((List) null).when(buildingService0).getAllBuilding();
      Injector.inject(buildingAPI0, (Class<?>) BuildingAPI.class, "buildingService", (Object) buildingService0);
      Injector.validateBean(buildingAPI0, (Class<?>) BuildingAPI.class);
      List<Building> list0 = buildingAPI0.getAllBuilding();
      assertNull(list0);
  }
}