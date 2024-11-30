package com.example.recipefinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.entities.Recipe
import com.squareup.picasso.Picasso

class RecipeAdapter(
    private val onRecipeClick: (Recipe) -> Unit // Callback for item clicks
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private val recipes = mutableListOf<Recipe>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int = recipes.size

    fun updateRecipes(newRecipes: List<Recipe>) {
        recipes.clear()
        recipes.addAll(newRecipes)
        notifyDataSetChanged()
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recipeImage: ImageView = itemView.findViewById(R.id.iv_recipe_image)
        private val recipeName: TextView = itemView.findViewById(R.id.tv_recipe_name)

        fun bind(recipe: Recipe) {
            recipeName.text = recipe.recipeName
            Picasso.get()
                .load(recipe.imageUri) // Replace Glide with Picasso
                .placeholder(R.drawable.background_placeholder) // Optional: Add a placeholder image
//                .error(R.drawable.error_image) // Optional: Add an error image
                .fit() // Scales the image to fit ImageView dimensions
                .centerCrop() // Centers and crops the image
                .into(recipeImage)

            itemView.setOnClickListener {
                onRecipeClick(recipe)
            }
        }
    }
}
