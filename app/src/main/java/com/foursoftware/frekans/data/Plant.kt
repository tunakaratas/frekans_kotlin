package com.foursoftware.frekans.data
enum class PlantCategory {
    HERBS,
    VEGETABLES,
    FRUITS,
    FLOWERS
}
data class Plant(
    val id: Int,
    val name: String,
    val turkishName: String,
    val frequency: Double,
    val frequencies: List<Double> = emptyList(),
    val description: String,
    val icon: String = "🌱",
    val category: PlantCategory = PlantCategory.HERBS
)
object PlantRepository {
    private fun generateFakeFrequencies(baseFreq: Double): List<Double> {
        return listOf(
            baseFreq,
            baseFreq * 1.2 + kotlin.random.Random.nextDouble(10.0, 50.0),
            baseFreq * 0.8 + kotlin.random.Random.nextDouble(5.0, 30.0),
            baseFreq * 1.5 + kotlin.random.Random.nextDouble(20.0, 80.0),
            baseFreq * 0.6 + kotlin.random.Random.nextDouble(15.0, 45.0)
        ).map { it.coerceIn(200.0, 2000.0) }
    }
    val plants = listOf(
        Plant(
            id = 1,
            name = "Basil",
            turkishName = "Fesleğen",
            frequency = 528.0,
            frequencies = generateFakeFrequencies(528.0),
            description = "Fesleğen için şifa frekansı. Büyümeyi destekler ve enerji verir.",
            icon = "🌿",
            category = PlantCategory.HERBS
        ),
        Plant(
            id = 3,
            name = "Lavender",
            turkishName = "Lavanta",
            frequency = 528.0,
            frequencies = generateFakeFrequencies(528.0),
            description = "Lavanta için sakinleştirici frekans. Kök gelişimini destekler.",
            icon = "💜",
            category = PlantCategory.HERBS
        ),
        Plant(
            id = 5,
            name = "Mint",
            turkishName = "Nane",
            frequency = 528.0,
            frequencies = generateFakeFrequencies(528.0),
            description = "Nane için canlandırıcı frekans. Yaprak gelişimini hızlandırır.",
            icon = "🌱",
            category = PlantCategory.HERBS
        ),
        Plant(
            id = 11,
            name = "Rosemary",
            turkishName = "Biberiye",
            frequency = 528.0,
            frequencies = generateFakeFrequencies(528.0),
            description = "Biberiye için güçlendirici frekans. Kök sistemini güçlendirir.",
            icon = "🌿",
            category = PlantCategory.HERBS
        ),
        Plant(
            id = 12,
            name = "Thyme",
            turkishName = "Kekik",
            frequency = 432.0,
            frequencies = generateFakeFrequencies(432.0),
            description = "Kekik için doğal frekans. Aromatik özellikleri artırır.",
            icon = "🌿",
            category = PlantCategory.HERBS
        ),
        Plant(
            id = 13,
            name = "Oregano",
            turkishName = "Kekik Otu",
            frequency = 528.0,
            frequencies = generateFakeFrequencies(528.0),
            description = "Kekik otu için büyüme frekansı. Yaprak kalitesini iyileştirir.",
            icon = "🌿",
            category = PlantCategory.HERBS
        ),
        Plant(
            id = 2,
            name = "Tomato",
            turkishName = "Domates",
            frequency = 432.0,
            frequencies = generateFakeFrequencies(432.0),
            description = "Domates bitkisi için doğal büyüme frekansı. Meyve vermeyi artırır.",
            icon = "🍅",
            category = PlantCategory.VEGETABLES
        ),
        Plant(
            id = 7,
            name = "Cucumber",
            turkishName = "Salatalık",
            frequency = 528.0,
            frequencies = generateFakeFrequencies(528.0),
            description = "Salatalık için büyüme frekansı. Su alımını optimize eder.",
            icon = "🥒",
            category = PlantCategory.VEGETABLES
        ),
        Plant(
            id = 8,
            name = "Pepper",
            turkishName = "Biber",
            frequency = 639.0,
            frequencies = generateFakeFrequencies(639.0),
            description = "Biber için enerji frekansı. Acılığı ve aromayı geliştirir.",
            icon = "🌶️",
            category = PlantCategory.VEGETABLES
        ),
        Plant(
            id = 9,
            name = "Lettuce",
            turkishName = "Marul",
            frequency = 432.0,
            frequencies = generateFakeFrequencies(432.0),
            description = "Marul için yumuşak frekans. Yaprak gelişimini destekler.",
            icon = "🥬",
            category = PlantCategory.VEGETABLES
        ),
        Plant(
            id = 14,
            name = "Carrot",
            turkishName = "Havuç",
            frequency = 528.0,
            frequencies = generateFakeFrequencies(528.0),
            description = "Havuç için kök gelişim frekansı. Toprak altı büyümeyi destekler.",
            icon = "🥕",
            category = PlantCategory.VEGETABLES
        ),
        Plant(
            id = 15,
            name = "Broccoli",
            turkishName = "Brokoli",
            frequency = 432.0,
            frequencies = generateFakeFrequencies(432.0),
            description = "Brokoli için beslenme frekansı. Vitamin içeriğini artırır.",
            icon = "🥦",
            category = PlantCategory.VEGETABLES
        ),
        Plant(
            id = 6,
            name = "Strawberry",
            turkishName = "Çilek",
            frequency = 432.0,
            frequencies = generateFakeFrequencies(432.0),
            description = "Çilek için doğal frekans. Meyve kalitesini artırır.",
            icon = "🍓",
            category = PlantCategory.FRUITS
        ),
        Plant(
            id = 16,
            name = "Apple Tree",
            turkishName = "Elma Ağacı",
            frequency = 528.0,
            frequencies = generateFakeFrequencies(528.0),
            description = "Elma ağacı için büyüme frekansı. Meyve verimini artırır.",
            icon = "🍎",
            category = PlantCategory.FRUITS
        ),
        Plant(
            id = 17,
            name = "Lemon Tree",
            turkishName = "Limon Ağacı",
            frequency = 639.0,
            frequencies = generateFakeFrequencies(639.0),
            description = "Limon ağacı için canlandırıcı frekans. C vitamini içeriğini artırır.",
            icon = "🍋",
            category = PlantCategory.FRUITS
        ),
        Plant(
            id = 4,
            name = "Rose",
            turkishName = "Gül",
            frequency = 639.0,
            frequencies = generateFakeFrequencies(639.0),
            description = "Gül için aşk ve büyüme frekansı. Çiçeklenmeyi teşvik eder.",
            icon = "🌹",
            category = PlantCategory.FLOWERS
        ),
        Plant(
            id = 10,
            name = "Sunflower",
            turkishName = "Ayçiçeği",
            frequency = 741.0,
            frequencies = generateFakeFrequencies(741.0),
            description = "Ayçiçeği için güneş enerjisi frekansı. Büyümeyi hızlandırır.",
            icon = "🌻",
            category = PlantCategory.FLOWERS
        ),
        Plant(
            id = 18,
            name = "Tulip",
            turkishName = "Lale",
            frequency = 528.0,
            frequencies = generateFakeFrequencies(528.0),
            description = "Lale için çiçeklenme frekansı. Renk canlılığını artırır.",
            icon = "🌷",
            category = PlantCategory.FLOWERS
        ),
        Plant(
            id = 19,
            name = "Jasmine",
            turkishName = "Yasemin",
            frequency = 528.0,
            frequencies = generateFakeFrequencies(528.0),
            description = "Yasemin için koku frekansı. Aromatik özellikleri güçlendirir.",
            icon = "🌸",
            category = PlantCategory.FLOWERS
        ),
        Plant(
            id = 20,
            name = "Orchid",
            turkishName = "Orkide",
            frequency = 639.0,
            frequencies = generateFakeFrequencies(639.0),
            description = "Orkide için zarif büyüme frekansı. Çiçek ömrünü uzatır.",
            icon = "🌺",
            category = PlantCategory.FLOWERS
        ),
        Plant(id = 21, name = "Sage", turkishName = "Adaçayı", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Adaçayı için şifa frekansı. Yaprak kalitesini artırır.", icon = "🌿", category = PlantCategory.HERBS),
        Plant(id = 22, name = "Dill", turkishName = "Dereotu", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Dereotu için büyüme frekansı. Aromatik özellikleri güçlendirir.", icon = "🌿", category = PlantCategory.HERBS),
        Plant(id = 23, name = "Parsley", turkishName = "Maydanoz", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Maydanoz için canlandırıcı frekans. Vitamin içeriğini artırır.", icon = "🌿", category = PlantCategory.HERBS),
        Plant(id = 24, name = "Cilantro", turkishName = "Kişniş", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Kişniş için doğal frekans. Yaprak gelişimini destekler.", icon = "🌿", category = PlantCategory.HERBS),
        Plant(id = 25, name = "Tarragon", turkishName = "Tarhun", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Tarhun için aromatik frekans. Koku kalitesini artırır.", icon = "🌿", category = PlantCategory.HERBS),
        Plant(id = 26, name = "Chives", turkishName = "Frenk Soğanı", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Frenk soğanı için büyüme frekansı. Kök gelişimini destekler.", icon = "🧄", category = PlantCategory.HERBS),
        Plant(id = 27, name = "Bay Leaf", turkishName = "Defne Yaprağı", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Defne için şifa frekansı. Yaprak kalitesini iyileştirir.", icon = "🌿", category = PlantCategory.HERBS),
        Plant(id = 28, name = "Fennel", turkishName = "Rezene", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Rezene için doğal frekans. Kök sistemini güçlendirir.", icon = "🌿", category = PlantCategory.HERBS),
        Plant(id = 29, name = "Marjoram", turkishName = "Mercanköşk", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Mercanköşk için aromatik frekans. Büyümeyi hızlandırır.", icon = "🌿", category = PlantCategory.HERBS),
        Plant(id = 30, name = "Chervil", turkishName = "Frenk Maydanozu", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Frenk maydanozu için yumuşak frekans. Yaprak gelişimini destekler.", icon = "🌿", category = PlantCategory.HERBS),
        Plant(id = 31, name = "Lemon Balm", turkishName = "Oğul Otu", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Oğul otu için sakinleştirici frekans. Yaprak kalitesini artırır.", icon = "🌿", category = PlantCategory.HERBS),
        Plant(id = 32, name = "Chamomile", turkishName = "Papatya", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Papatya için şifa frekansı. Çiçeklenmeyi teşvik eder.", icon = "🌼", category = PlantCategory.HERBS),
        Plant(id = 33, name = "Echinacea", turkishName = "Ekinezya", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Ekinezya için güçlendirici frekans. Kök gelişimini destekler.", icon = "🌿", category = PlantCategory.HERBS),
        Plant(id = 34, name = "Ginseng", turkishName = "Ginseng", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Ginseng için enerji frekansı. Kök kalitesini artırır.", icon = "🌿", category = PlantCategory.HERBS),
        Plant(id = 35, name = "Turmeric", turkishName = "Zerdeçal", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Zerdeçal için şifa frekansı. Kök büyümesini destekler.", icon = "🌿", category = PlantCategory.HERBS),
        Plant(id = 36, name = "Ginger", turkishName = "Zencefil", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Zencefil için canlandırıcı frekans. Kök gelişimini hızlandırır.", icon = "🌿", category = PlantCategory.HERBS),
        Plant(id = 37, name = "Ginkgo", turkishName = "Ginkgo", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Ginkgo için büyüme frekansı. Yaprak kalitesini iyileştirir.", icon = "🌿", category = PlantCategory.HERBS),
        Plant(id = 38, name = "St. John's Wort", turkishName = "Sarı Kantaron", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Sarı kantaron için şifa frekansı. Çiçeklenmeyi artırır.", icon = "🌼", category = PlantCategory.HERBS),
        Plant(id = 39, name = "Valerian", turkishName = "Kediotu", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Kediotu için sakinleştirici frekans. Kök sistemini güçlendirir.", icon = "🌿", category = PlantCategory.HERBS),
        Plant(id = 40, name = "Milk Thistle", turkishName = "Deve Dikeni", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Deve dikeni için şifa frekansı. Büyümeyi destekler.", icon = "🌿", category = PlantCategory.HERBS        ),
        Plant(id = 41, name = "Onion", turkishName = "Soğan", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Soğan için kök gelişim frekansı. Soğan başı büyümesini artırır.", icon = "🧅", category = PlantCategory.VEGETABLES),
        Plant(id = 42, name = "Garlic", turkishName = "Sarımsak", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Sarımsak için güçlendirici frekans. Diş gelişimini destekler.", icon = "🧄", category = PlantCategory.VEGETABLES),
        Plant(id = 43, name = "Potato", turkishName = "Patates", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Patates için kök büyüme frekansı. Yumru gelişimini artırır.", icon = "🥔", category = PlantCategory.VEGETABLES),
        Plant(id = 44, name = "Spinach", turkishName = "Ispanak", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Ispanak için beslenme frekansı. Yaprak kalitesini iyileştirir.", icon = "🥬", category = PlantCategory.VEGETABLES),
        Plant(id = 45, name = "Kale", turkishName = "Kara Lahana", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Kara lahana için güçlendirici frekans. Vitamin içeriğini artırır.", icon = "🥬", category = PlantCategory.VEGETABLES),
        Plant(id = 46, name = "Cabbage", turkishName = "Lahana", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Lahana için büyüme frekansı. Baş gelişimini destekler.", icon = "🥬", category = PlantCategory.VEGETABLES),
        Plant(id = 47, name = "Cauliflower", turkishName = "Karnabahar", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Karnabahar için beslenme frekansı. Çiçek gelişimini artırır.", icon = "🥦", category = PlantCategory.VEGETABLES),
        Plant(id = 48, name = "Radish", turkishName = "Turp", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Turp için kök büyüme frekansı. Kök gelişimini hızlandırır.", icon = "🥕", category = PlantCategory.VEGETABLES),
        Plant(id = 49, name = "Beet", turkishName = "Pancar", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Pancar için kök gelişim frekansı. Renk kalitesini artırır.", icon = "🥕", category = PlantCategory.VEGETABLES),
        Plant(id = 50, name = "Turnip", turkishName = "Şalgam", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Şalgam için büyüme frekansı. Kök sistemini güçlendirir.", icon = "🥕", category = PlantCategory.VEGETABLES),
        Plant(id = 51, name = "Celery", turkishName = "Kereviz", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Kereviz için su alım frekansı. Sap gelişimini destekler.", icon = "🥬", category = PlantCategory.VEGETABLES),
        Plant(id = 52, name = "Asparagus", turkishName = "Kuşkonmaz", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Kuşkonmaz için büyüme frekansı. Sürgün kalitesini artırır.", icon = "🥬", category = PlantCategory.VEGETABLES),
        Plant(id = 53, name = "Artichoke", turkishName = "Enginar", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Enginar için çiçek gelişim frekansı. Baş büyümesini destekler.", icon = "🥬", category = PlantCategory.VEGETABLES),
        Plant(id = 54, name = "Zucchini", turkishName = "Kabak", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Kabak için meyve verme frekansı. Verimliliği artırır.", icon = "🥒", category = PlantCategory.VEGETABLES),
        Plant(id = 55, name = "Eggplant", turkishName = "Patlıcan", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Patlıcan için büyüme frekansı. Meyve kalitesini iyileştirir.", icon = "🍆", category = PlantCategory.VEGETABLES),
        Plant(id = 56, name = "Okra", turkishName = "Bamya", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Bamya için meyve gelişim frekansı. Verimliliği artırır.", icon = "🥬", category = PlantCategory.VEGETABLES),
        Plant(id = 57, name = "Corn", turkishName = "Mısır", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Mısır için büyüme frekansı. Koçan gelişimini destekler.", icon = "🌽", category = PlantCategory.VEGETABLES),
        Plant(id = 58, name = "Beans", turkishName = "Fasulye", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Fasulye için meyve verme frekansı. Bakla gelişimini artırır.", icon = "🫘", category = PlantCategory.VEGETABLES),
        Plant(id = 59, name = "Peas", turkishName = "Bezelye", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Bezelye için büyüme frekansı. Bakla kalitesini iyileştirir.", icon = "🫛", category = PlantCategory.VEGETABLES),
        Plant(id = 60, name = "Lentil", turkishName = "Mercimek", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Mercimek için kök gelişim frekansı. Verimliliği artırır.", icon = "🫘", category = PlantCategory.VEGETABLES),
        Plant(id = 61, name = "Chickpea", turkishName = "Nohut", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Nohut için büyüme frekansı. Tane gelişimini destekler.", icon = "🫘", category = PlantCategory.VEGETABLES),
        Plant(id = 62, name = "Soybean", turkishName = "Soya Fasulyesi", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Soya fasulyesi için protein frekansı. Tane kalitesini artırır.", icon = "🫘", category = PlantCategory.VEGETABLES),
        Plant(id = 63, name = "Pumpkin", turkishName = "Balkabağı", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Balkabağı için meyve büyüme frekansı. Verimliliği artırır.", icon = "🎃", category = PlantCategory.VEGETABLES),
        Plant(id = 64, name = "Squash", turkishName = "Sakız Kabağı", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Sakız kabağı için büyüme frekansı. Meyve gelişimini destekler.", icon = "🥒", category = PlantCategory.VEGETABLES),
        Plant(id = 65, name = "Sweet Potato", turkishName = "Tatlı Patates", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Tatlı patates için kök büyüme frekansı. Yumru kalitesini artırır.", icon = "🍠", category = PlantCategory.VEGETABLES        ),
        Plant(id = 66, name = "Orange Tree", turkishName = "Portakal Ağacı", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Portakal ağacı için meyve frekansı. C vitamini içeriğini artırır.", icon = "🍊", category = PlantCategory.FRUITS),
        Plant(id = 67, name = "Grapefruit Tree", turkishName = "Greyfurt Ağacı", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Greyfurt ağacı için büyüme frekansı. Meyve kalitesini iyileştirir.", icon = "🍊", category = PlantCategory.FRUITS),
        Plant(id = 68, name = "Mandarin Tree", turkishName = "Mandalin Ağacı", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Mandalin ağacı için meyve frekansı. Verimliliği artırır.", icon = "🍊", category = PlantCategory.FRUITS),
        Plant(id = 69, name = "Peach Tree", turkishName = "Şeftali Ağacı", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Şeftali ağacı için çiçeklenme frekansı. Meyve gelişimini destekler.", icon = "🍑", category = PlantCategory.FRUITS),
        Plant(id = 70, name = "Apricot Tree", turkishName = "Kayısı Ağacı", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Kayısı ağacı için büyüme frekansı. Meyve kalitesini artırır.", icon = "🍑", category = PlantCategory.FRUITS),
        Plant(id = 71, name = "Cherry Tree", turkishName = "Kiraz Ağacı", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Kiraz ağacı için çiçeklenme frekansı. Meyve verimini artırır.", icon = "🍒", category = PlantCategory.FRUITS),
        Plant(id = 72, name = "Plum Tree", turkishName = "Erik Ağacı", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Erik ağacı için meyve frekansı. Büyümeyi destekler.", icon = "🍑", category = PlantCategory.FRUITS),
        Plant(id = 73, name = "Pear Tree", turkishName = "Armut Ağacı", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Armut ağacı için büyüme frekansı. Meyve kalitesini iyileştirir.", icon = "🍐", category = PlantCategory.FRUITS),
        Plant(id = 74, name = "Grape Vine", turkishName = "Üzüm Asması", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Üzüm asması için meyve frekansı. Salkım gelişimini destekler.", icon = "🍇", category = PlantCategory.FRUITS),
        Plant(id = 75, name = "Watermelon", turkishName = "Karpuz", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Karpuz için büyüme frekansı. Meyve kalitesini artırır.", icon = "🍉", category = PlantCategory.FRUITS),
        Plant(id = 76, name = "Melon", turkishName = "Kavun", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Kavun için meyve frekansı. Aroma gelişimini destekler.", icon = "🍈", category = PlantCategory.FRUITS),
        Plant(id = 77, name = "Raspberry", turkishName = "Ahududu", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Ahududu için büyüme frekansı. Meyve kalitesini iyileştirir.", icon = "🫐", category = PlantCategory.FRUITS),
        Plant(id = 78, name = "Blueberry", turkishName = "Yaban Mersini", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Yaban mersini için meyve frekansı. Antioksidan içeriğini artırır.", icon = "🫐", category = PlantCategory.FRUITS),
        Plant(id = 79, name = "Blackberry", turkishName = "Böğürtlen", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Böğürtlen için büyüme frekansı. Meyve verimini artırır.", icon = "🫐", category = PlantCategory.FRUITS),
        Plant(id = 80, name = "Cranberry", turkishName = "Kızılcık", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Kızılcık için meyve frekansı. Vitamin içeriğini artırır.", icon = "🫐", category = PlantCategory.FRUITS),
        Plant(id = 81, name = "Gooseberry", turkishName = "Bektaşi Üzümü", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Bektaşi üzümü için büyüme frekansı. Meyve kalitesini iyileştirir.", icon = "🫐", category = PlantCategory.FRUITS),
        Plant(id = 82, name = "Currant", turkishName = "Kuş Üzümü", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Kuş üzümü için meyve frekansı. Salkım gelişimini destekler.", icon = "🫐", category = PlantCategory.FRUITS),
        Plant(id = 83, name = "Elderberry", turkishName = "Mürver", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Mürver için büyüme frekansı. Meyve verimini artırır.", icon = "🫐", category = PlantCategory.FRUITS),
        Plant(id = 84, name = "Fig Tree", turkishName = "İncir Ağacı", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "İncir ağacı için meyve frekansı. Meyve kalitesini iyileştirir.", icon = "🫐", category = PlantCategory.FRUITS),
        Plant(id = 85, name = "Pomegranate Tree", turkishName = "Nar Ağacı", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Nar ağacı için büyüme frekansı. Tane gelişimini destekler.", icon = "🍎", category = PlantCategory.FRUITS),
        Plant(id = 86, name = "Persimmon Tree", turkishName = "Trabzon Hurması", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Trabzon hurması için meyve frekansı. Olgunlaşmayı hızlandırır.", icon = "🍎", category = PlantCategory.FRUITS),
        Plant(id = 87, name = "Quince Tree", turkishName = "Ayva Ağacı", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Ayva ağacı için büyüme frekansı. Meyve kalitesini artırır.", icon = "🍎", category = PlantCategory.FRUITS),
        Plant(id = 88, name = "Mulberry Tree", turkishName = "Dut Ağacı", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Dut ağacı için meyve frekansı. Salkım gelişimini destekler.", icon = "🫐", category = PlantCategory.FRUITS),
        Plant(id = 89, name = "Date Palm", turkishName = "Hurma Ağacı", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Hurma ağacı için büyüme frekansı. Meyve verimini artırır.", icon = "🍎", category = PlantCategory.FRUITS),
        Plant(id = 90, name = "Olive Tree", turkishName = "Zeytin Ağacı", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Zeytin ağacı için meyve frekansı. Zeytin kalitesini iyileştirir.", icon = "🫒", category = PlantCategory.FRUITS        ),
        Plant(id = 91, name = "Daisy", turkishName = "Papatya", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Papatya için çiçeklenme frekansı. Çiçek kalitesini artırır.", icon = "🌼", category = PlantCategory.FLOWERS),
        Plant(id = 92, name = "Lily", turkishName = "Zambak", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Zambak için büyüme frekansı. Çiçeklenmeyi teşvik eder.", icon = "🌺", category = PlantCategory.FLOWERS),
        Plant(id = 93, name = "Iris", turkishName = "Süsen", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Süsen için çiçek frekansı. Renk canlılığını artırır.", icon = "🌺", category = PlantCategory.FLOWERS),
        Plant(id = 94, name = "Peony", turkishName = "Şakayık", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Şakayık için çiçeklenme frekansı. Çiçek büyümesini destekler.", icon = "🌺", category = PlantCategory.FLOWERS),
        Plant(id = 95, name = "Dahlia", turkishName = "Yıldız Çiçeği", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Yıldız çiçeği için büyüme frekansı. Çiçek kalitesini iyileştirir.", icon = "🌺", category = PlantCategory.FLOWERS),
        Plant(id = 96, name = "Chrysanthemum", turkishName = "Kasımpatı", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Kasımpatı için çiçeklenme frekansı. Çiçek ömrünü uzatır.", icon = "🌼", category = PlantCategory.FLOWERS),
        Plant(id = 97, name = "Carnation", turkishName = "Karanfil", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Karanfil için çiçek frekansı. Koku kalitesini artırır.", icon = "🌺", category = PlantCategory.FLOWERS),
        Plant(id = 98, name = "Geranium", turkishName = "Sardunya", frequency = 528.0, frequencies = generateFakeFrequencies(528.0), description = "Sardunya için büyüme frekansı. Çiçeklenmeyi teşvik eder.", icon = "🌺", category = PlantCategory.FLOWERS),
        Plant(id = 99, name = "Petunia", turkishName = "Petunya", frequency = 639.0, frequencies = generateFakeFrequencies(639.0), description = "Petunya için çiçeklenme frekansı. Renk çeşitliliğini artırır.", icon = "🌺", category = PlantCategory.FLOWERS),
        Plant(id = 100, name = "Begonia", turkishName = "Begonya", frequency = 432.0, frequencies = generateFakeFrequencies(432.0), description = "Begonya için büyüme frekansı. Çiçek kalitesini iyileştirir.", icon = "🌺", category = PlantCategory.FLOWERS)
    )
    fun getPlantById(id: Int): Plant? {
        return plants.find { it.id == id }
    }
    fun searchPlants(query: String): List<Plant> {
        if (query.isBlank()) return plants
        val lowerQuery = query.lowercase()
        return plants.filter {
            it.turkishName.lowercase().contains(lowerQuery) ||
            it.name.lowercase().contains(lowerQuery) ||
            it.description.lowercase().contains(lowerQuery)
        }
    }
    fun getPlantsByCategory(category: PlantCategory?): List<Plant> {
        return if (category == null) {
            plants
        } else {
            plants.filter { it.category == category }
        }
    }
}
