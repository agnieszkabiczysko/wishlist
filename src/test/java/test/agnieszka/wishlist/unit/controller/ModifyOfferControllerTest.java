package test.agnieszka.wishlist.unit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import agnieszka.wishlist.controller.ModifyOfferController;
import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.service.OfferService;

@RunWith(MockitoJUnitRunner.class)
public class ModifyOfferControllerTest {

	@InjectMocks
	private ModifyOfferController controller;
	
	@Mock
	private OfferService offerService;
	
	@Mock
	private CurrentUserHelper currentUserHelper;
	
	@Spy
	private ModelMap model;
	
	@Mock
	private Principal principal;
	
	@Test
	public void showAddNewOfferForm() {
		//when
		Offer newOffer = new Offer();
		String viewName = controller.showNewOfferForm(model, newOffer);
		
		//then
		assertThat(viewName).isEqualTo("addNewOffer");
		verify(model, times(1)).addAttribute("offer", newOffer);
	}
	
	@Test
	public void processAddNewOfferFormWithFailure() {
		//given
		Offer newOffer = offer(user());
		
		BindingResult result = mock(BindingResult.class);
		when(result.hasErrors()).thenReturn(true);
		
		//when
		String viewName = controller.addOffer(newOffer, result, principal);
		
		//then
		assertThat(viewName).isEqualTo("addNewOffer");
	}

	
	@Test
	public void processAddNewOfferFormWithSuccess() throws IOException {
		//given
		Offer offer = offer(user());
		addMockImage(offer);
		
		BindingResult result = mock(BindingResult.class);
		when(result.hasErrors()).thenReturn(false);
		
		User user = user();
		when(currentUserHelper.getCurrentUser(principal)).thenReturn(user);
		
		//when
		String viewName = controller.addOffer(offer, result, principal);
		
		//then
		assertThat(viewName).isEqualTo("redirect:/offers");
		verify(offerService, times(1)).update(offer);
		assertThat(offer.getOfferSeller()).isEqualTo(user);
		verify(offer.getImage(), times(1)).transferTo(any(File.class));
	}
	
	@Test
	public void editFormShouldBeForbiddenForNonOwner() {
		//given
		User user = user();
		when(currentUserHelper.getCurrentUser(principal)).thenReturn(user);
		
		Offer offer = offer(anotherUser());
		
		when(offerService.findOfferById(1)).thenReturn(offer);
		
		//when
		String view = controller.editOffer(1, model, principal);
		
		//then
		assertThat(view).isEqualTo("redirect:/accessDenied");
		assertThat(model.get("offer")).isNull();;
	}
	
	@Test
	public void editFormShouldBeAllowedforOfferSeler() {
		//given
		User user = user();
		when(currentUserHelper.getCurrentUser(principal)).thenReturn(user);
		
		Offer offer = offer(user);

		when(offerService.findOfferById(2)).thenReturn(offer);
		
		//when
		String viewName = controller.editOffer(2, model, principal);
		
		//then
		assertThat(viewName).isEqualTo("addNewOffer");
		assertThat(model.get("offer")).isEqualTo(offer);
		assertThat(model.get("edit")).isEqualTo(true);
	}
	
	@Test
	public void updateOfferWithoutSuccess() {
		//given
		Offer offer = offer(user());

		BindingResult result = mock(BindingResult.class);
		when(result.hasErrors()).thenReturn(true);
		
		//when
		String viewName = controller.updateOffer(3, model, offer, result);
		
		//then
		assertThat(viewName).isEqualTo("addNewOffer");
	}
	
	@Test
	public void updateOfferWithSuccess() throws IOException {
		//given
		Offer offer = offer(user());
		addMockImage(offer);

		BindingResult result = mock(BindingResult.class);
		when(result.hasErrors()).thenReturn(false);
		
		//when
		String viewName = controller.updateOffer(4, model, offer, result);
		
		//then
		assertThat(viewName).isEqualTo("redirect:/offers");
		verify(offerService, times(1)).update(offer);
		verify(offer.getImage(), times(1)).transferTo(any(File.class));
	}
	
	private void addMockImage(Offer offer) {
		MultipartFile mockImage = mock(MultipartFile.class);
		when(mockImage.isEmpty()).thenReturn(false);
		offer.setImage(mockImage);
	}

	private User user() {
		User u1 = new User();
		u1.setFirstname("User1");
		u1.setLastname("Testowy");
		u1.setUserId("Testowy1");
		return u1;
	}
	
	private User anotherUser() {
		User u2 = new User();
		u2.setFirstname("User2");
		u2.setLastname("Testowy");
		u2.setUserId("Testowy2");
		return u2;
	}

	private Offer offer(User user) {
		Offer o1 = new Offer();
		o1.setId(1);
		o1.setName("Oferta1");
		o1.setVendor("Testowy");
		o1.setDescription("Testujemy");
		o1.setOfferSeller(user);
		return o1;
	}

}
