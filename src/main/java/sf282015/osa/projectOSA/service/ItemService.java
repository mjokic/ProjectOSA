package sf282015.osa.projectOSA.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sf282015.osa.projectOSA.entity.Item;
import sf282015.osa.projectOSA.repository.ItemRepository;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;
	
	
	public List<Item> getItems(){
		return (List<Item>) itemRepository.findAll();
	}
	
	public Item getItem(long id){
		return itemRepository.findOne(id);
	}
	
	public Item addItem(Item item){
		return itemRepository.save(item);
	}
	
	public Item editItem(long id, Item item){
		item.setId(id);
		return itemRepository.save(item);
	}
	
	public void deleteItem(long id){
		itemRepository.delete(id);
	}
	
}
