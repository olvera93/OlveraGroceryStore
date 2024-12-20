package com.olvera.groceries.service.impl

import com.olvera.groceries.dto.Hypermarket
import com.olvera.groceries.error.SupermarketException
import com.olvera.groceries.model.Supermarket
import com.olvera.groceries.repository.SupermarketRepository
import com.olvera.groceries.service.SupermarketService
import org.springframework.stereotype.Service

@Service
class SupermarketServiceImpl(
    private val supermarketRepository: SupermarketRepository,
) : SupermarketService {

    override fun findSupermarketByName(name: Hypermarket): Supermarket {
        return supermarketRepository.findByName(name) ?: throw SupermarketException("Supermarket with name $name not found")
    }
}