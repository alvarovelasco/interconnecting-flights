package com.ryanair.alvaro.interconnectingflights;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ryanair.alvaro.interconnectingflights.logic.ScheduleAbsoluteRouteInterconnectionResolverImplService;
import com.ryanair.alvaro.interconnectingflights.model.json.Route;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class InterconnectingFlightsApplicationTests {

	private MockMvc mockMvc;

    @Mock
    private ScheduleAbsoluteRouteInterconnectionResolverImplService mockedInterconnectionResolver;
    
    @Autowired
    @InjectMocks
    private InterconnectionSchedulesController interconnectionSchedulesController;
 
    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(interconnectionSchedulesController)
                .build();
    }
    
    @Test
    public void invokeInterconnection() throws Exception {
    	LocalDateTime departure = LocalDateTime.now().plusDays(1);
    	LocalDateTime arrival   = departure.plusDays(1);
        mockMvc.perform(get("/interconnections?departure={1}&arrival={2}&departureDateTime={3}&arrivalDateTime={4}",
        				"CPH", "MAD", departure, arrival))
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(),                        
                        Charset.forName("ISO-8859-1")                     
                        )));
 
        verify(mockedInterconnectionResolver, Mockito.times(1)).resolve(eq(Route.get("CPH","MAD")), eq(departure), eq(arrival));
        Mockito.verifyNoMoreInteractions(mockedInterconnectionResolver);
    }
    
	
}
