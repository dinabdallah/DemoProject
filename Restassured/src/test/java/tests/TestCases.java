package tests;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItem;

public class TestCases {

    @Test
    public void test() {
        given().baseUri("https://reqres.in/").
                when().get("/api/users?page=2").
                then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("page",equalTo(2))
                .assertThat().body("total",equalTo(12))
                .assertThat().body("total_pages",equalTo(2))
                .assertThat().body("data.first_name[0]",equalTo("Michael"))
                .assertThat().body("data.first_name",hasItems("Lindsay","Tobias","Byron"),
                        "data.last_name",hasItem("Fields"))
                .assertThat().body("id",not(hasItem(567)))
                .assertThat().body("data.first_name",contains("Michael","Lindsay","Tobias","Byron","George","Rachel"))
                .assertThat().body("data",not(empty()))
                .assertThat().body("data.id",hasSize(6)).log().body();
    }
    @Test
    public void GetsingleUser(){

        Response res=
                given()
                        .baseUri("https://reqres.in/")
                        .when()
                        .get("/api/users/2")
                        .then()
                        .extract().response();
        System.out.println(res.asString());  //extract all responce

        //Extract some data from responce
        String responce=res.path("data.email");
        System.out.println("email"+responce);
    }
    @Test
    public void SingleUserNotFound(){
        given()
                .baseUri("https://reqres.in")
                .when()
                .get("/api/users/23")
                .then()
                .assertThat().statusCode(404).log().body();
    }
    @Test
    public void ListResource(){
        given()
                .baseUri("https://reqres.in")
                .when()
                .get("/api/unknown")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("page",equalTo(1),
                        "data.id",hasSize(6)).log().body();

    }

    @Test
    public void GetSingleResource(){
        given()
                .baseUri("https://reqres.in")
                .when()
                .get("/api/unknown/2")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("data.name",equalTo("fuchsia rose")).log().body();
    }
    @Test
    public void CreateUsers(){
        given()
                .baseUri("https://reqres.in")
                .body("{ \n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"leader\"\n" +
                        "}")
                .when()
                .post("/api/users")
                .then().log().all()
                .assertThat().statusCode(201);
    }
    @Test
    public void UpdateUsers(){
        Response res= given()
                .baseUri("https://reqres.in")
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}")
                .when()
                .put("/api/users/2")
                .then()
                .extract().response();
        System.out.println(res.asString());

    }
    @Test
    public void Deleteuser(){
        given()
                .baseUri("https://reqres.in")
                .when()
                .delete("/api/users/2")
                .then()
                .assertThat().statusCode(204).log().body();

    }
    @Test
    public void RegisterSuccessful(){
        Response res =given()
                .baseUri("https://reqres.in")
                .header("Content-Type","application/json")
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}")
                .when()
                .post("/api/register")
                .then().extract().response();
        System.out.println(res.asString());
    }

    @Test
    public void registerUnSuccesful(){
        given()
                .header("Content-Type","application/json")
                .baseUri("https://reqres.in")
                .body("{\n" +
                        "    \"email\": \"sydney@fife\"\n" +
                        "}")
                .when()
                .post("/api/register")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",equalTo("Missing password")).log().body();
    }
    @Test
    public void LoginSuccessful(){
        given()
                .baseUri("https://reqres.in")
                .header("Content-Type","application/json")
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}")
                .when()
                .post("/api/login")
                .then()
                .assertThat().statusCode(200).log().body();
    }
    @Test
    public void LoginUnSuccessful(){
        given()
                .baseUri("https://reqres.in")
                .header("Content-Type","application/json")
                .body("{\n" +
                        "    \"email\": \"peter@klaven\"\n" +
                        "}")
                .when()
                .post("/api/login")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",equalTo("Missing password")).log().body();

    }
    @Test
    public void DelayedResponce(){
        given()
                .baseUri("https://reqres.in")
                .when()
                .get("/api/users?delay=3")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("total"
                        ,equalTo(12),"data.id",hasSize(6))
                .body("data.email",hasItem("george.bluth@reqres.in"))
                .body("data.first_name",hasItem("Janet"))
                .body("data.last_name",hasItem("Bluth"))
                .body("data.avatar",hasItem("https://reqres.in/img/faces/1-image.jpg")).log().body();

    }
}
