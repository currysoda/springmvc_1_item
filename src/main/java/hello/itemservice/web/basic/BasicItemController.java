package hello.itemservice.web.basic;


import hello.itemservice.domain.Item;
import hello.itemservice.domain.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

	private final ItemRepository itemRepository;

	@GetMapping()
	public String item(Model model) {
		List<Item> items = itemRepository.findAll();
		model.addAttribute("items", items);
		return "basic/items";
	}

	//테스트용 데이터 추가
	@PostConstruct
	public void init() {
		// 테스트용 데이터
		itemRepository.save(new Item("itemA", 10000, 100));
		itemRepository.save(new Item("itemB", 20000, 200));

	}

	@GetMapping("/{itemId}")
	public String item(@PathVariable("itemId") Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "basic/item";
	}

	@GetMapping("/add")
	public String addForm() {
		return "basic/addForm";
	}

//	@PostMapping("/add")
//	public String addItemV1(@RequestParam("itemName") String itemName,
//	                   @RequestParam("price") int price,
//	                   @RequestParam("quantity") Integer quantity,
//	                   Model model) {
//		Item item = new Item();
//		item.setItemName(itemName);
//		item.setPrice(price);
//		item.setQuantity(quantity);
//
//		itemRepository.save(item);
//
//		model.addAttribute("item", item);
//
//
//		return "basic/item";
//	}

//	@PostMapping("/add")
//	public String addItemV2(@ModelAttribute("item") Item item,
//	                        Model model) {
//
//		itemRepository.save(item);
//		model.addAttribute("item", item); // 자동 추가됨
//
//		return "basic/item";
//	}

	// P R G 적용 (POST Redirect GET)
//	@PostMapping("/add")
//	public String addItemV2(@ModelAttribute("item") Item item,
//	                        Model model) {
//
//		itemRepository.save(item);
//		model.addAttribute("item", item); // 자동 추가됨
//
//		return "redirect:/basic/items/" + item.getId();
//	}

	// RedirectAttribute 적용
	@PostMapping("/add")
	public String addItemV2(@ModelAttribute("item") Item item,
	                        Model model,
	                        RedirectAttributes redirectAttributes) {

		Item savedItem = itemRepository.save(item);
		model.addAttribute("item", item); // 자동 추가됨

		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status",true)

		return "redirect:/basic/items/{itemId}";
	}

	/**
	 *  -parameter 를 해줘야 작동하는 코드
	 *  생략이 과하면 알아보기 힘들다는 문제점이 있지만 일단 생략 가능하다.
	 */
	/*@PostMapping("/add")
	public String addItemV3(@ModelAttribute Item item,
	                        Model model) {

		itemRepository.save(item);
		model.addAttribute("item", item); // 자동 추가됨

		return "basic/item";
	}*/

//	@PostMapping("/add")
////	public String addItemV4(Item item) {
////
////		itemRepository.save(item);
//////		model.addAttribute("item", item); // 자동 추가됨
////
////		return "basic/item";
////	}

	@GetMapping("/{itemId}/edit")
	public String editForm(@PathVariable("itemId") Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "basic/editForm";
	}

	@PostMapping("/{itemId}/edit")
	public String edit(@PathVariable("itemId") Long itemId,@ModelAttribute("item") Item item) {
		itemRepository.update(itemId, item);
		return "redirect:/basic/items/{itemId}";
	}
}
