package org.mitre.springboot.openid.connect.web;

import javax.transaction.Transactional;

import org.junit.Test;

@Transactional
public class StatsApiAuthorizationTests extends ApiAuthorizationTestsBase{

    @Test
    public void adminGetApiStatsSuccess() throws Exception{
        adminSession();
        checkGetAccess("/api/stats/byclientid/1", 200);
    }

    @Test
    public void userGetApiStatsSuccess() throws Exception{
        userSession();
        checkGetAccess("/api/stats/byclientid/1", 200);
    }

    @Test
    public void anonymousGetApiStatsUnauthenticated() throws Exception{
        checkGetAccess("/api/stats/byclientid/1", 401);
    }

    @Test
    public void adminGetApiStatsSummarySuccess() throws Exception{
        adminSession();
        checkGetAccess("/api/stats/summary", 200);
    }

    @Test
    public void userGetApiStatsSummarySuccess() throws Exception{
        userSession();
        checkGetAccess("/api/stats/summary", 200);
    }

    @Test
    public void anonymousGetApiStatsSummarySuccess() throws Exception{
        checkGetAccess("/api/stats/summary", 200);
    }

}
