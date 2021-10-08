# Restaurant-Ordering-App
A restaurant ordering application for Android designed to allow customers to make their orders and payments while sitting in their tables. 

The application has three screens (activities). The first one will display your tables as Buttons. The second one will be activated by tapping on a table (Button), in order to accept an order from that table. The third one will be activated by long pressing on a table (Button), in order to accept a payment from that table.

Note 1: A table may make multiple orders (e.g. when the company orders another round, or a new customer joins the company). CTower is responsible for merging these multiple orders.

Note 2: A table may pay an order partially (e.g. when a member of the company leaves earlier). CTower is responsible for monitoring the remainder of a table.

This code includes:

1. Database Setup
2. Button initialization
3. Querying and modifying database entries
4. OnClick and OnLongClick events
5. Using Adapter and RecyclerView

Note 3: This app is a coursework assignment. It queries a server that is not personally maintained by me and thus, may not work anymore if the webmaster decides to deactivate this service.

![roa2](https://user-images.githubusercontent.com/66119148/136571644-9f15e252-5b35-4eb6-bc77-ff54a0bb188c.JPG)
