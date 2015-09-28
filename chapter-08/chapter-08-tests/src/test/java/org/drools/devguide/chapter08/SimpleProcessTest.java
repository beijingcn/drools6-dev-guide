/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drools.devguide.chapter08;

import java.util.HashMap;
import java.util.Map;
import org.drools.devguide.BaseTest;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.runtime.process.WorkflowProcessInstance;

/**
 *
 * @author salaboy
 */
public class SimpleProcessTest extends BaseTest {

     /**
     * A simple Approval process with a Human task It uses two process
     * variables: 
     * 1) requested_amount represent the amount requested to be
     * approved 
     * 2) request_status represent the status of the request after the
     * manager's evaluation
     * 
     * This is the rejected scenario
     */
    @Test
    public void testSimpleBPMN2Rejected() {
        System.out.println(" ###### Starting Simple BPMN2 ###### ");

        KieSession ksession = this.createDefaultSession();

        ksession.addEventListener(new SystemOutProcessEventListener());
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task",
                new ManagerApprovalSimpleWorkItemHandler());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("requested_amount", 1005);
        System.out.println(" ###### >>> Requesting $1005 ###### ");
        ProcessInstance processInstance = ksession.startProcess("simple", params);

        WorkflowProcessInstance wpi = (WorkflowProcessInstance) processInstance;

        assertThat(processInstance, notNullValue());
        assertThat(ProcessInstance.STATE_COMPLETED, is(processInstance.getState()));
        assertThat("rejected", is(wpi.getVariable("request_status")));
        System.out.println(" ###### >>> Request Status " + wpi.getVariable("request_status") + " ###### ");
        System.out.println(" ###### Completed Simple BPMN2 ###### ");
    }

    /**
     * A simple Approval process with a Human task It uses two process
     * variables: 
     *  1) requested_amount represent the amount requested to be
     * approved 
     *  2) request_status represent the status of the request after the
     * manager's evaluation
     * 
     * This is the approved scenario
     */
    @Test
    public void testSimpleBPMN2Approved() {
        System.out.println(" ###### Starting Simple BPMN2 ###### ");

        KieSession ksession = this.createDefaultSession();

        ksession.addEventListener(new SystemOutProcessEventListener());
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task",
                new ManagerApprovalSimpleWorkItemHandler());
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("requested_amount", 900);
        System.out.println(" ###### >>> Requesting $900 ###### ");
        ProcessInstance processInstance = ksession.startProcess("simple", params);

        WorkflowProcessInstance wpi = (WorkflowProcessInstance) processInstance;

        assertThat(processInstance, notNullValue());
        assertThat(ProcessInstance.STATE_COMPLETED, is(processInstance.getState()));
        assertThat("approved", is(wpi.getVariable("request_status")));
        System.out.println(" ###### >>> Request Status " + wpi.getVariable("request_status") + " ###### ");
        System.out.println(" ###### Completed Simple BPMN2 ###### ");

    }

    

    private class ManagerApprovalSimpleWorkItemHandler implements WorkItemHandler {

        @Override
        public void executeWorkItem(WorkItem wi, WorkItemManager wim) {
            String actorId = (String) wi.getParameters().get("ActorId");
            if (actorId.equals("manager")) {
                Integer amount = (Integer) wi.getParameters().get("amount");

                System.out.println(" >>> Here the Manager reviewing requested amount");
                System.out.println(" >>> \t Requested Amount: $" + amount);
                Map<String, Object> results = new HashMap<String, Object>();
                if (amount >= 1000) {
                    System.out.println(" >>> \t But I can approve until $1000, so I'm rejecting the request");
                    results.put("status", "rejected");
                    wim.completeWorkItem(wi.getId(), results);
                } else {
                    System.out.println(" >>> \t I'm approving the request because is less than $1000");
                    results.put("status", "approved");
                    wim.completeWorkItem(wi.getId(), results);
                }
            }

        }

        @Override
        public void abortWorkItem(WorkItem wi, WorkItemManager wim) {

        }

    }

}
