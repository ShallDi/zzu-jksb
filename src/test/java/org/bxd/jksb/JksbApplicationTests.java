package org.bxd.jksb;

class JksbApplicationTests {
    public static void main(String[] args) {
        test("22g4");
    }

    private static void test(String name) {

        System.out.println(name.replaceAll("^(\\w{" + (name.length() / 2 - 2 )+ "})\\w{4}", "$1****"));

    }

    void contextLoads() {
//        System.out.println(repository.save(new User("hrx", "201812272013584", "04190024", "河南省郑州市郑州大学新校区松园1号楼522宿舍")));
//        System.out.println(repository.save(new User("bhb", "201722362013943", "10120356", "科学大道100号")));
//        System.out.println(repository.save(new User("dxz", "201722362014037", "12435678", "郑州大学新校区")));
//        System.out.println(repository.save(new User("wxp", "201822362014456", "10123551", "郑州市科学大道郑州大学新校区")));
//        System.out.println(repository.save(new User("zy", "201722362014089", "02097213", "郑州大学新校区松园")));
//        System.out.println(repository.save(new User("yq", "201712362013792", "09266027", "郑州市科学大道100号郑州大学新校区")));
    }

}
