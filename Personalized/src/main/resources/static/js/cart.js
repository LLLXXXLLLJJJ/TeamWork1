
function ShoppingCarObserver(elInput, isAdd) {
	this.elInput = elInput
	this.parents = this.elInput.parents('.goods-item')
	this.count = parseInt(this.elInput.val())//count:购物车Id

	//新增的两个值
	this.goodsId = parseInt($(this.elInput).attr("goodsId"))
	this.price = parseFloat($(this.elInput).attr("price"))

	this.isAdd = isAdd
	this.singlePrice = parseFloat(this.parents.find('.single-price').text())
	this.number=parseInt(this.parents.find('.goods-count').val())

	this.computeGoodsMoney = function() {
		var moneyCount = this.count * this.singlePrice
		var singleTotalEl = this.parents.find('.single-total')
		console.log(moneyCount)
		singleTotalEl.empty().append(moneyCount)
	}

	this.showCount = function() {
		var isChecked = this.parents.find('.goods-list-item')[0].checked
		var GoodsTotalMoney = parseFloat($('#selectGoodsMoney').text())
		var goodsTotalCount = parseInt($('#selectGoodsCount').text())
		if(this.elInput) {
			if(this.isAdd) {
				++this.count
				if(isChecked) {
					$('#selectGoodsMoney').empty().append(GoodsTotalMoney + this.singlePrice)
					$('#selectGoodsCount').empty().append(goodsTotalCount + 1)
				}
			} else {
				if(parseInt(this.count) <= 1) {
					return
				} else {
					--this.count
					if(isChecked) {
						$('#selectGoodsMoney').empty().append(GoodsTotalMoney - this.singlePrice)
						$('#selectGoodsCount').empty().append(goodsTotalCount - 1)
					}
				}
			}
			this.elInput.val(this.count)
		}
	}

	this.checkIsAll = function() {
		var checkLen = $('.goods-list-item:checked').length
		if (checkLen > 0) {
			$('.submitData').removeClass('submitDis')
		} else {
			$('.submitData').addClass('submitDis')
		}
		if($('.goods-item').length === checkLen) {
			$('#checked-all-bottom, #check-goods-all').prop('checked', true)
		} else {
			$('#checked-all-bottom, #check-goods-all').prop('checked', false)
		}
	}
	this.checkedChange = function(isChecked) {
		if(isChecked === undefined) {
			var isChecked = this.parents.find('.goods-list-item')[0].checked
		}
		var itemTotalMoney = parseFloat(this.parents.find('.single-total').text())
		var GoodsTotalMoney = parseFloat($('#selectGoodsMoney').text())
		var itemCount = parseInt(this.parents.find('.goods-count').val())
		var goodsTotalCount = parseInt($('#selectGoodsCount').text())
		if(isChecked) {
			$('#selectGoodsMoney').empty().append(itemTotalMoney + GoodsTotalMoney)
			$('#selectGoodsCount').empty().append(itemCount + goodsTotalCount)
		} else {
			if (GoodsTotalMoney - itemTotalMoney === 0) {
				$('#selectGoodsMoney').empty().append('0.00')
				if (!$('.submitData').hasClass('submitDis')) {
					$('.submitData').addClass('submitDis')
				}
			} else {
				$('#selectGoodsMoney').empty().append(GoodsTotalMoney - itemTotalMoney)
			}
			$('#selectGoodsCount').empty().append(goodsTotalCount - itemCount)
		}
	}
	this.deleteGoods = function() {
		var isChecked = this.parents.find('.goods-list-item')[0].checked
		if(isChecked) {
			this.checkedChange(false)
		}
		this.parents.remove()

		this.checkOptions()
	}
	this.checkOptions = function() {
		if ($('#check-goods-all')[0].checked) {
			if ($('.goods-list-item').length <= 0) {
				$('#checked-all-bottom, #check-goods-all').prop('checked', false)
			}
		}
	}
}
function checkedAll(_this) {
	if ($('.goods-item').length <= 0) {
		$('.submitData').addClass('submitDis')
	} else {
		$('.submitData').removeClass('submitDis')
	}
	for(var i = 0; i < $('.goods-item').length; i++) {
		var elInput = $('.goods-item').eq(i).find('.goods-list-item')
		var isChecked = $('.goods-item').eq(i).find('.goods-list-item')[0].checked
		var checkAllEvent = new ShoppingCarObserver(elInput, null)
		if(_this.checked) {
			if(!isChecked) {
				elInput.prop('checked', true)
				checkAllEvent.checkedChange(true)
			}
		} else {
			if (!$('.submitData').hasClass('submitDis')){
				$('.submitData').addClass('submitDis')
			}
			if(isChecked) {
				elInput.prop('checked', false)
				checkAllEvent.checkedChange(false)
			}
		}
	}
}
$('#check-goods-all').on('change', function() {
	if(this.checked) {
		$('#checked-all-bottom').prop('checked', true)
	} else {
		$('#checked-all-bottom').prop('checked', false)
	}
	checkedAll(this)
})
$('#checked-all-bottom').on('change', function() {
	if(this.checked) {
		$('#check-goods-all').prop('checked', true)
	} else {
		$('#check-goods-all').prop('checked', false)
	}
	checkedAll(this)
})
$('.goods-list-item').on('change', function() {
	var tmpCheckEl = $(this)
	var checkEvent = new ShoppingCarObserver(tmpCheckEl, null)
	checkEvent.checkedChange()
	checkEvent.checkIsAll()
})



//“-”按钮
$('.goods-content').on('click', '.car-decrease', function() {
	var goodsInput = $(this).parents('.input-group').find('.goods-count')
	var decreaseCount = new ShoppingCarObserver(goodsInput, false)
	decreaseCount.showCount()
	decreaseCount.computeGoodsMoney()
})
//“+”按钮
$('.goods-content').on('click', '.car-add', function() {
	var goodsInput = $(this).parents('.input-group').find('.goods-count')
	var addCount = new ShoppingCarObserver(goodsInput, true)
	addCount.showCount()
	addCount.computeGoodsMoney()
})





//商品删除按钮
$('.goods-content').on('click', '.item-delete', function() {
	var goodsInput = $(this).parents('.goods-item').find('.goods-list-item')
	deleteGoods = new ShoppingCarObserver(goodsInput, null)
	$('#deleteItemTip').modal('show')
})

//商品删除，确定按钮
$('.deleteSure').on('click', function() {
	if(deleteGoods !== null) {
		//ajax代码实现
		$.ajax({
			type:"POST",
			url:"/cart/deleteCartByIds/"+deleteGoods.count,
			//请求 URL: http://localhost:8080/cart/deleteCartByIds/89
		});
		deleteGoods.deleteGoods()
	}
	$('#deleteItemTip').modal('hide')
})










//批量删除商品按钮
$('#deleteMulty').on('click', function() {
	if($('.goods-list-item:checked').length <= 0) {
		$('#selectItemTip').modal('show')
	} else {
		$('#deleteMultyTip').modal('show')
	}
})




// //批量删除商品,确定按钮
// $('.deleteMultySure').on('click', function() {
// 	var Ids="";
// 	for (var i = 0; i < $('.goods-list-item:checked').length; i++) {
// 		var multyDelete = new ShoppingCarObserver($('.goods-list-item:checked').eq(i), null)
// 		multyDelete.deleteGoods()
// 		Ids=Ids+multyDelete.count+","
// 		i--
// 	}
// 	//ajax代码实现
// 	$.ajax({
// 		type:"POST",
// 		url:"/cart/deleteIds/"+Ids,
// 		//http://localhost:8080/cart/deleteCartByIds/35,46,
// 	});
// 	multyDelete.checkOptions()
// 	$('#deleteMultyTip').modal('hide')
// })


//批量删除商品,确定按钮
$('.deleteMultySure').on('click', function() {
	var Ids="";
	for (var i = 0; i < $('.goods-list-item:checked').length; i++) {
		var multyDelete = new ShoppingCarObserver($('.goods-list-item:checked').eq(i), null)
		multyDelete.deleteGoods()
		Ids=Ids+multyDelete.count+","
		i--
	}
	console.log(Ids)
	//ajax代码实现
	$.ajax({
		type:"POST",
		url:"/cart/deleteIds/"+Ids,
		//http://localhost:8080/cart/deleteCartByIds/35,46,
	});
	multyDelete.checkOptions()
	$('#deleteMultyTip').modal('hide')
})




















//结算按钮
$('#updateMulty').on('click', function() {
	if($('.goods-list-item:checked').length <= 0) {
		$('#selectItemTip').modal('show')
	} else {
		$('#updateMultyTip').modal('show')
	}
})



// //批量购买商品，结算，确定按钮
// $('.updateMultySure').on('click', function() {
// 	var myArray = new Array();
// 	for (var i = 0; i < $('.goods-list-item:checked').length; i++) {
// 		var multyUpdate = new ShoppingCarObserver($('.goods-list-item:checked').eq(i), null)
// 		multyUpdate.deleteGoods()
// 		myArray.push({id:multyUpdate.count,number:multyUpdate.number})
// 		i--
// 	}
//
// 	console.log(JSON.stringify(myArray))
// 	//ajax代码实现
// 	$.ajax({
// 		type:"POST",
// 		url:"/cart/buyGoods",
// 		data:JSON.stringify(myArray),
// 		dataType:"json",
// 		contentType:"application/json",
// 		success : function(result) {
// 			//alert(result);
// 		}
// 	});
// 	multyUpdate.checkOptions()
// 	$('#updateMultyTip').modal('hide')
// })

//批量购买商品，结算，确定按钮
$('.updateMultySure').on('click', function() {
	var myArray = new Array();
	for (var i = 0; i < $('.goods-list-item:checked').length; i++) {
		var multyUpdate = new ShoppingCarObserver($('.goods-list-item:checked').eq(i), null)
		multyUpdate.deleteGoods()
		myArray.push({cartId:multyUpdate.count,num:multyUpdate.number, price:multyUpdate.price,goodsId:multyUpdate.goodsId})
		i--
	}

	console.log(JSON.stringify(myArray))
	//ajax代码实现
	$.ajax({
		type:"POST",
		url:"/order/cart",
		data:JSON.stringify(myArray),
		//[{cartId: 70, num: 1, price: 179, goodsId: 1051002},…]
		dataType:"json",
		contentType:"application/json",
		success : function(result) {
			console.log(JSON.stringify(result))
		}
	});
	multyUpdate.checkOptions()
	$('#updateMultyTip').modal('hide')
})