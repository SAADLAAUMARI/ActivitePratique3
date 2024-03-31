# Spring MVC JPA Hibernate Spring Data Thymeleaf
# Objectif 
L'objectif de cette activté est de créer dans la premiere partie une application Web JEE basée sur Spring MVC, Thylemeaf et Spring Data JPA qui permet de gérer les patients. Dans la deuxiéme partie, on va sécurisé notre application à l'aide de spring security.
# Gestion Patient | Application Web
Nous allons prendre en charge le projet de gestion des patients que nous allons effectuer dans le cadre de l'activité 3, puis nous allons le transformer en application Web. qui vq permet de réaliser ses fonctionnalié suivante: 
- Afficher les patients.
- Faire la pagination.
- Chercher les patients.
- Supprimer un patient.
- Créer une page template
- Faire la validation des formulaires
De plus,
```Intégration de Spring Security 6``` qui renforce la sécurité de l'application en assurant l'authentification, l'autorisation et la protection contre les utilisateurs non autorisés.
## Dependencies

Ajout les dépendances suivantes au projet ``` pom.xml ``` pour améliorer la sécurité de l'application:

``` xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

```
## Configuration de la securite 
Dans le package ```ma.enset.project.security```, on va ajouter une classe intitulée ```SecurityConfig```qui contiendra la configuration de la securite de notre application.
```java 
@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.formLogin().loginPage("/login").permitAll();
        httpSecurity.rememberMe();
        httpSecurity.authorizeHttpRequests().requestMatchers("/webjars/**","/h2-console/**").permitAll();
        httpSecurity.authorizeHttpRequests().requestMatchers("/user/**").hasRole("USER");
        httpSecurity.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN");
        httpSecurity.authorizeHttpRequests().anyRequest().authenticated();
        httpSecurity.exceptionHandling().accessDeniedPage("/notAuthorized");
        return httpSecurity.build();
    }

``` 
> Cette fonction configure les autorisations dans l'application, distinguant les rôles d'administrateur et d'utilisateur. De plus, elle permet l'accès aux ressources webjars et à la console H2 sans nécessiter d'authentification.
## Creation des utlisateurs 

``` java
@Bean
   public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
       return new InMemoryUserDetailsManager(
               User.withUsername("user1").password(passwordEncoder.encode("1234")).roles("USER").build(),
               User.withUsername("user2").password(passwordEncoder.encode("1234")).roles("USER").build(),
               User.withUsername("admin").password(passwordEncoder.encode("1234")).roles("USER","ADMIN").build()
       );
   }

```

## Encoder le mot de passe 

Dans la classe ```ProjectApplication```

``` java 
  @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
```
Dans la classe ```SecurityConfig```,on ajoute:
``` java 
@Autowired
private PasswordEncoder passwordEncoder;

```
> - cette methode permet de faire le hashage du mot de passe.
> - pour encoder le mot de passe, nous utilisons l'instruction suivant ```passwordEncoder.encode("1234")```.

### Navbar 
Pour afficher une navbae bien organisée avec des boutons et un dropdown contenat Nouveau et chercher. De plus, un autre dropdown avec le nom d'utlisateur de l'utilisateur connecté qui contient le Logout:

``` html
<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
    <div class="container-fluid">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link active" th:href="@{/user/index}">Home</a>
            </li>

            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">Produits</a>
                <ul class="dropdown-menu">
                    <li th:if="${#authorization.expression('hasRole(''ADMIN'')')}"><a class="dropdown-item" th:href="@{/admin/formPatients}">Nouveau</a></li>
                    <li><a class="dropdown-item" th:href="@{/user/index}">Chercher</a></li>
                </ul>
            </li>
        </ul>
        <ul class="navbar-nav">
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" th:text="${#authentication.name}">
                    [Username]</a>
                <ul class="dropdown-menu">
                    <li>
                        <form method="post" th:action="@{/logout}">
                            <button class="dropdown-item" type="submit">Logout</button>
                        </form>
                        </li>
                </ul>
            </li>
        </ul>
    </div>
</nav>
```
![image](https://github.com/SAADLAAUMARI/ActivitePratique3/assets/133123024/818f390a-9d14-4733-b2f0-8e5620efdd3e)

### Droit d'acces
Pour vérifier les droits d'acces de l'utilisateur connecté : 
``` html 
<td th:if="${#authorization.expression('hasRole(''ADMIN'')')}">
                        <a class="btn btn-success" th:href="@{/admin/editPatient(id=${p.id}, keyword=${keyword},page=${currentPage})}">Edit</a>
                    </td>
```
> ce bouton ne sera affiché que si l'utilisateur est admin.

# Application

# Page de connexion:

![image](https://github.com/SAADLAAUMARI/ActivitePratique3/assets/133123024/52891b8d-a6eb-42ed-bb9f-9b21fb48d35b)


## Utilisateur avec Role Admin 

![image](https://github.com/SAADLAAUMARI/ActivitePratique3/assets/133123024/a3a412de-513d-4f38-9232-b4e44e92b5f2)

Ajouter un utilisateur:
  ![image](https://github.com/SAADLAAUMARI/ActivitePratique3/assets/133123024/ae659bc5-47ab-4819-b564-70beeeff9976)
  -Si non respect des contraintes de l'ajout, des messages en rouge s'affichent:
![image](https://github.com/SAADLAAUMARI/ActivitePratique3/assets/133123024/c5eda36a-ffa9-444a-8615-275064a1cf03)
  -En cas de respect des contraintes, l'utilisateur sera ajjouté.

 Modifier un utilisateur:
 ![image](https://github.com/SAADLAAUMARI/ActivitePratique3/assets/133123024/ca965ac9-5cd8-4645-8acd-6d63015bbe1e)

 Supprimer un utilisateur:
 ![image](https://github.com/SAADLAAUMARI/ActivitePratique3/assets/133123024/7ea2345d-422c-4fa0-b9f0-800dfad870a0)
 > Un message de confirmation s'affiche
 L'utilisateur ainsi sera supprimé en cliquant sur 'OK':
![image](https://github.com/SAADLAAUMARI/ActivitePratique3/assets/133123024/43c2d0d0-1297-4de9-b533-36569914bba8)

Chercher un utilisateur:
![image](https://github.com/SAADLAAUMARI/ActivitePratique3/assets/133123024/90c1be19-66a7-438b-98ee-98412b86c904)

En cliquant sur logout sur le dropdown ```admin```, l'utilisateur sera déconnecté et redirigé vers la page de connexion.

## Utilisateur avec Role User 

![image](https://github.com/SAADLAAUMARI/ActivitePratique3/assets/133123024/4641652a-9b89-4811-bff9-2c88c087dd7d)

