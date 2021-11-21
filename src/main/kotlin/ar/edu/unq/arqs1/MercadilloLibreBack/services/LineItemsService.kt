package ar.edu.unq.arqs1.MercadilloLibreBack.services

import ar.edu.unq.arqs1.MercadilloLibreBack.models.LineItem
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Order
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.line_item.LineItemRepository
import org.springframework.stereotype.Service

@Service
class LineItemsService(val lineItemRepository: LineItemRepository) {
    fun addLineItem(lineItem: LineItem): Result<LineItem> {
        return try {
            validateOrder(lineItem)
            validateProduct(lineItem)
            validateProductStock(lineItem.product, lineItem.quantity)
            lineItem.price = lineItem.product.price
            Result.success(lineItemRepository.save(lineItem))
        } catch (e:Exception) {
            Result.failure(e)
        }
    }

    private fun validateProduct(lineItem: LineItem) {
        if(!lineItem.product.isActive)
            throw LineItem.ProductDeactivated()
    }

    private fun validateOrder(lineItem: LineItem) {
        if(lineItem.order!!.isCharged()) {
            throw Order.OrderCharged()
        }
    }

    fun deleteLineItem(lineItem: LineItem): Result<Unit> {
        return try {
            validateOrder(lineItem)
            lineItemRepository.delete(lineItem)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    fun updateQuantity(lineItem: LineItem, quantity: Int): Result<LineItem> {
        return try {
            validateOrder(lineItem)
            validateProductStock(lineItem.product, quantity)
            lineItem.quantity = quantity
            Result.success(lineItemRepository.save(lineItem))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun validateProductStock(product: Product, quantity: Int) {
        if (!product.canSupplyStockFor(quantity)) {
            throw Product.MissingStock()
        }
    }
}