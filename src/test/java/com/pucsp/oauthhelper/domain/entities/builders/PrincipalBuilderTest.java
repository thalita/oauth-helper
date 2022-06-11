package com.pucsp.oauthhelper.domain.entities.builders;

import com.pucsp.oauthhelper.domain.entities.Principal;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PrincipalBuilderTest {
    @Test
    public void deveRetornarPrincipalBuilderQuandoAcionado(){
        Principal builder = PrincipalBuilder.build("AAAA444", "45454848");
        assertEquals("AAAA444", builder.getIdentifier());
        assertEquals("45454848", builder.getCode());
    }

}