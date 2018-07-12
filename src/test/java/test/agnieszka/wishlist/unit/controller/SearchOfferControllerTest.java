package test.agnieszka.wishlist.unit.controller;

import static agnieszka.wishlist.model.SearchType.NAME;
import static agnieszka.wishlist.model.SearchType.VENDOR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;

import agnieszka.wishlist.controller.SearchOfferController;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.service.OfferService;

@RunWith(MockitoJUnitRunner.class)
public class SearchOfferControllerTest {
	
	@InjectMocks
	private SearchOfferController controller;
	
	@Mock
	private OfferService offerService;

	@Test
	public void shouldShowSearchForm() {
		//when
		String view = controller.showSearchOfferForm();
		
		//then
		assertThat(view).isEqualTo("findOffer");
	}
	
	@Test
	public void searchForOfferNameShouldReturnResults() {
		//given
		ModelMap model = spy(ModelMap.class);
		String searchFor = "test search";
		String searchTypeStr = NAME.name();
		List<Offer> offers = offers();
		
		when(offerService.findOfferByName(searchFor)).thenReturn(offers);
		
		//when
		String view = controller.showSearchOfferResults(model, searchFor, searchTypeStr);
		
		//then
		assertThat(view).isEqualTo("offers");
		verify(offerService, times(1)).findOfferByName(searchFor);
		assertThat(model.get("offers")).isEqualTo(offers);
	}
	
	@Test
	public void searchForOfferVendorShouldReturnResults() {
		//given
		ModelMap model = spy(ModelMap.class);
		String searchFor = "test search";
		String searchTypeStr = VENDOR.name();
		List<Offer> offers = offers();
		
		when(offerService.findOfferByVendor(searchFor)).thenReturn(offers);
		
		//when
		String view = controller.showSearchOfferResults(model, searchFor, searchTypeStr);
		
		//then
		assertThat(view).isEqualTo("offers");
		verify(offerService, times(1)).findOfferByVendor(searchFor);
		assertThat(model.get("offers")).isEqualTo(offers);
	}
	
	private List<Offer> offers() {
		List<Offer> offers = new ArrayList<>();
		offers.add(offer("A"));
		offers.add(offer("B"));
		return offers;
	}
	
	private Offer offer(String name) {
		Offer offer = new Offer();
		offer.setName(name);
		return offer;
	}
}
