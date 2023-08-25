/*
Если перейти на сайт https://www.boredapi.com/documentation,
то там так и написано, что при запросе Sample Query: http://www.boredapi.com/api/activity/ мы получим ответ
{
	"activity": "Learn Express.js",
	"accessibility": 0.25,
	"type": "education",
	"participants": 1,
	"price": 0.1,
	"link": "https://expressjs.com/",
	"key": "3943506"
}
просто переписываем в класс название переменных и их типы, это и будет наш ответ
 */

data class ActivityResponse(
    val activity: String,
    val accessibility: Double,
    val type: String,
    val participants: Int,
    val price: Double,
    val link: String,
    val key: String
)