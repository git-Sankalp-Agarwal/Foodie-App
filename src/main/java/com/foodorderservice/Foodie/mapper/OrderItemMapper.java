package com.foodorderservice.Foodie.mapper;

import com.foodorderservice.Foodie.dtos.OrderItemDTO;
import com.foodorderservice.Foodie.entity.OrderItem;
import org.mapstruct.Mapper;
import java.util.List;


@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItem toEntity(OrderItemDTO dto);
    OrderItemDTO toDto(OrderItem entity);
    List<OrderItemDTO> toDtos(List<OrderItem> entities);
    List<OrderItem> toEntities(List<OrderItemDTO> dtos);
}
