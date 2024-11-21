//package com.example.recipefinder
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//
//// Data class for recipe items
//data class Recipe(
//    val id: String,
//    val name: String,
//    val cookingTime: String,
//    val difficulty: String,
//    val imageUrl: String,
//    val calories: Int,
//    val servings: Int
//)
//
//class RecipeAdapter(
//    private var recipes: List<Recipe>,
//    private val onRecipeClick: (Recipe) -> Unit
//) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
//
//    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val recipeImage: ImageView = itemView.findViewById(R.id.recipeImage)
//        val recipeName: TextView = itemView.findViewById(R.id.recipeName)
//        val cookingTime: TextView = itemView.findViewById(R.id.cookingTime)
//        val difficulty: TextView = itemView.findViewById(R.id.difficulty)
//        val calories: TextView = itemView.findViewById(R.id.calories)
//        val servings: TextView = itemView.findViewById(R.id.servings)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_recipe, parent, false)
//        return RecipeViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
//        val recipe = recipes[position]
//
//        holder.recipeName.text = recipe.name
//        holder.cookingTime.text = recipe.cookingTime
//        holder.difficulty.text = recipe.difficulty
//        holder.calories.text = "${recipe.calories} cal"
//        holder.servings.text = "${recipe.servings} servings"
//
//        // TODO: Load image using your preferred image loading library
//        // Example with Glide:
//        // Glide.with(holder.recipeImage)
//        //     .load(recipe.imageUrl)
//        //     .into(holder.recipeImage)
//
//        holder.itemView.setOnClickListener {
//            onRecipeClick(recipe)
//        }
//    }
//
//    override fun getItemCount() = recipes.size
//
//    fun updateRecipes(newRecipes: List<Recipe>) {
//        recipes = newRecipes
//        notifyDataSetChanged()
//    }
//}