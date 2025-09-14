package com.foodorderservice.Foodie.mapper;

import com.foodorderservice.Foodie.dtos.OrderRequestDTO;
import com.foodorderservice.Foodie.dtos.OrderResponseDTO;
import com.foodorderservice.Foodie.entity.Order;
import com.foodorderservice.Foodie.entity.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


import java.util.ArrayList;
import java.util.List;


@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {


    Order toEntity(OrderRequestDTO dto);


    OrderResponseDTO toResponseDTO(Order order);


    List<OrderResponseDTO> toResponseDTOs(List<Order> orders);
    List<Order> toEntities(List<OrderRequestDTO> dtos);


    @AfterMapping
    default void linkOrderItems(OrderRequestDTO dto, @MappingTarget Order order) {
        if (order == null) return;


        List<OrderItem> items = order.getItems();
        if (items == null || items.isEmpty()) return;


        List<OrderItem> copy = new ArrayList<>(items);
        order.getItems().clear();
        for (OrderItem item : copy) {
            order.addItem(item);
        }
    }
}
