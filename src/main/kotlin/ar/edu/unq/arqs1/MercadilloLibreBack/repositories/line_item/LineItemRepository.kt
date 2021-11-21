package ar.edu.unq.arqs1.MercadilloLibreBack.repositories.line_item

import ar.edu.unq.arqs1.MercadilloLibreBack.models.LineItem
import org.springframework.data.jpa.repository.JpaRepository

interface LineItemRepository: JpaRepository<LineItem, Long>